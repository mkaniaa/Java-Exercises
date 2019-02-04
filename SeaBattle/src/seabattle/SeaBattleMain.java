package seabattle;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import seabattle.Board.Cell;

public class SeaBattleMain extends Application {

	private static Stage pStage;
	private boolean running = false;
	private Board enemyBoard, playerBoard;
	private HBox placeTools = null;
	private int[] allShips = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
	int shipToPlace = 0;
	private boolean enemyTurn = false;
	private Random random = new Random();
	private ArrayList<Label> shipsLabels = new ArrayList<Label>();
	private boolean isVertical = true;
	private Label title = new Label();
	private Label score = new Label();
	private boolean win = false;
	ChoiceBox<String> cb = null;
	
	private Parent createContent() {
		BorderPane root = new BorderPane();
		root.setPrefSize(1000, 700);
		
		//tworzneie tablicy przeciwnika
		enemyBoard = new Board(true, event -> {
			
			if (!running) return;
			
			Cell cell = (Cell)event.getSource();
			if (cell.wasShot) return;
			
			//jezeli gracz trafi to nadal bedzie trwal jego ruch, gdy zatopi statek, zmieni sie stan etykiety score
			enemyTurn = !cell.shoot(score, enemyBoard);
			
			//zakonczenie gry zwyciestwem gracza
			if (enemyBoard.ships == 0) {
				win = true;
				gameOver(win, root, title, pStage);
			}
			
			if (enemyTurn) {
				enemyMove(root, title);
			}
		});
		
		
		//tworzenie tablicy gracza		
		playerBoard = new Board(false, event -> {
			if(running) return;
			
			//warunek, aby po kliknieciu PPM nie zostal postawiony statek (PPM sluzy do zmiany orientacji statku)
			if(event.getButton() == MouseButton.SECONDARY) {
				return;
			}
			
			//umieszczanie statków przez gracza:
			Cell cell = (Cell) event.getSource();
			
			if (playerBoard.placeShip(new Ship(allShips[shipToPlace], isVertical), cell.x, cell.y)) {
				changeLabel();
				if (++shipToPlace == allShips.length) {
					//cofniecie do ostatniego rekordu tablicy statkow - potrzebne do rozlozenia statkow przeciwnika
					shipToPlace--;
					//zmiana komunikatu w tytule i uruchomienie metody rozlozkaldajacej statki
					title.setText("It's done! Now you can shoot on player side!");
					startGame();
				}
			}
		});

		//tworzenie tytu³u i narzêdzi
		title.setText("Place your ships!");
		title.setFont(new Font("Arial", 25));
		StackPane titlePane = new StackPane(title);
		placeTools = makeToolBox(score, enemyBoard);
		
		

		//sklejanie calej sceny
		HBox hbox = new HBox(50, playerBoard, enemyBoard);
		hbox.setAlignment(Pos.CENTER);
		root.setBottom(placeTools);
		root.setTop(titlePane);
		BorderPane.setMargin(titlePane, new Insets(20,0,0,0));
		BorderPane.setMargin(placeTools, new Insets(10,0,0,0));
		BorderPane.setAlignment(placeTools, Pos.CENTER);
		root.setCenter(hbox);
		
		return root;
	}
	
	private void enemyMove(BorderPane root, Label title) {
		while (enemyTurn) {
			int x = random.nextInt(12);
			int y = random.nextInt(12);
			
			Cell cell = playerBoard.getCell(x, y);
			if (cell.wasShot) continue;
			
			//jezeli przeciwnik trafi, to jego ruch nadal bedzie trwal
			enemyTurn = cell.shoot(score, enemyBoard);
			
			if (playerBoard.ships == 0) {
				gameOver(win, root, title, pStage); 
			}
		}
	}
	
	private void startGame() {

		//umieszczenie statków przez komputer:
		while (shipToPlace >= 0) {
			int x = random.nextInt(12);
			int y = random.nextInt(12);
			
			if(enemyBoard.placeShip(new Ship (allShips[shipToPlace], Math.random() < 0.5), x, y)) shipToPlace--;
		}
		running = true;
	}
	
	public void start (Stage primaryStage) throws Exception {
		Scene scene = new Scene(createContent());
		//dodanie do sceny listenera, aby mozna bylo zmieniac PPM stan orientacji (wymaganie w zadaniu)
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton() == MouseButton.SECONDARY) {
		        	if(cb.getValue().equals("vertical")) {
		        		cb.setValue("horizontal");
		        		isVertical = false;
		        	} else {
		        		cb.setValue("vertical");
		        		isVertical = true;
		        	}
		        }
		    }
		});
		pStage = primaryStage;
		primaryStage.setTitle("Sea Battle");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	//funkcja tworzaca okno z narzêdziami do dodawania statków
	public HBox makeToolBox(Label score, Board enemyBoard) {
		
		//wybór orientacji statku
		Label labOrient = new Label("Chose orientation:");
		cb = new ChoiceBox<String>(FXCollections.observableArrayList("vertical", "horizontal"));
		cb.setValue("vertical");
		
		//zmiana orientacji przez wybranie z listy
		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() == 1) isVertical = false;
				else isVertical = true;
			}
		});
		
		Label labSize = new Label("Ships left to place:");
		HBox choseOrient = new HBox(15, labOrient, cb);
		HBox.setMargin(labOrient, new Insets(4,0,0,0));
		
		//wizualizacja statkow do wyboru
		HBox boatsLeftPanel = new HBox();
		boatsLeftPanel = boatsLeftStats(allShips);
		
		VBox toolBox = new VBox();
		toolBox.setAlignment(Pos.CENTER_LEFT);
		toolBox.setSpacing(15);
		toolBox.getChildren().addAll(choseOrient, labSize, boatsLeftPanel);
		toolBox.setPrefWidth(400);
		HBox.setMargin(labSize, new Insets(0,0,0,0));
		
		//dodanie etykiety z iloscia statkow do zatopienia na planszy przeciwnika
		score.setText(enemyBoard.ships + " ships left on enemy field!" );
		score.setFont(new Font("Arial", 17));
		StackPane scorePane = new StackPane(score);
		scorePane.setAlignment(Pos.TOP_CENTER);
		
		HBox toolsWindow = new HBox();
		toolsWindow.setAlignment(Pos.CENTER_LEFT);
		toolsWindow.getChildren().addAll(toolBox, scorePane);
		HBox.setMargin(toolBox, new Insets(0,0,0,100));
		HBox.setMargin(scorePane, new Insets(0,0,0,28));
		return toolsWindow;
	}
	
	public HBox boatsLeftStats(int[] ships) {
		VBox boatsVbox = new VBox();
		boatsVbox.setSpacing(10);
		VBox labelsVbox = new VBox();
		int nextSize = 0;
		int countSize = 1;
		for(int i : ships) {
			if (nextSize == i) {
				countSize++;
			}
			else{
				nextSize = i;
			
				//dodanie etykiety dla nowego rozmiaru
				Label newLabel = new Label(countSize + "x left of: ");
				shipsLabels.add(newLabel);
				countSize = 2;
				
				//dodanie wizualizacji dla nowego rozmiaru
				HBox nextShip = new HBox();
				for(int j = 0; j < nextSize; j++) {
					Rectangle rect = new Rectangle(25,25);
					rect.setFill(Color.WHITE);
					rect.setStroke(Color.GREEN);
					nextShip.getChildren().add(rect);
				}
				boatsVbox.getChildren().add(nextShip);
				
			}
		}
		
		//tworzenie etykiet z iloscia pozostalych do umieszczenia na planszy statkow
		for (Label nextLabel : shipsLabels) {
			labelsVbox.getChildren().add(nextLabel);
			VBox.setMargin(nextLabel, new Insets(5,0,14,0));
		}
		return new HBox(10, labelsVbox, boatsVbox);
	}
	
	public void changeLabel() {
		//sprawdzenie pierwszej wartosci w kazdej z etykiet w tablicy, jezeli nie jest 0 to zmniejszenie jej o 1
		for (Label label : shipsLabels) {
			int value = Character.getNumericValue((label.getText().charAt(0)));
			if (value != 0) {
				label.setText((value-1) + "x left of: ");
				break;
			}
		}
	}
	
	private void gameOver(boolean win, BorderPane rootPane, Label label, Stage primaryStage){ 
		if(win) label.setText("YOU WIN!");
		else label.setText("YOU LOST!");
		Button playAgain = new Button("Play Again");
		playAgain.setOnAction(e -> {
			shipsLabels.clear();
			primaryStage.close();
			try {
				start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		StackPane gameOverPane = new StackPane(playAgain);
		gameOverPane.setAlignment(Pos.CENTER);
		rootPane.setCenter(gameOverPane);
	} 
	
	public static void main(String[] args) {
		launch(args);
	}
}
