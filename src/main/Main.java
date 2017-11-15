package main;

import java.util.Random;

import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

public class Main extends Application{
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 750;
	public static final int FRAME_RATE = 25;
	public static final int GRID_SIZE = 25;
	
	private Pane root;
	private Canvas canvasFood;
	private Canvas canvas;
	private Canvas canvas2;
	private Timeline timer;
	private Text result;
	
	private int[] x, y;
	private int dir;
	private int length;
	private boolean check;
	private double hp;
	private double maxHp;
	private ProgressBar hpBar;
	
	private int[] x2, y2;
	private int dir2;
	private int length2;
	private boolean check2;
	private double hp2;
	private double maxHp2;
	private ProgressBar hpBar2;
	
	private int[] xFood, yFood;
	private int nFood;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		root = new Pane();
		canvasFood = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		canvas2 = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
		result = new Text();
		result.setFont(new Font(100));
		result.setX(SCREEN_WIDTH/2 - 300);
		result.setY(SCREEN_HEIGHT/2 + 25);
		
		x = new int[1000];
		y = new int[1000];
		x[0] = 100;
		y[0] = SCREEN_HEIGHT / 2;
		dir = 1;
		length = 1;
		hp = 100;
		maxHp = 100;
		hpBar = new ProgressBar(1);
		hpBar.setPrefSize(350, 40);
		hpBar.relocate(10, 10);
		hpBar.setOpacity(0.5);
		
		x2 = new int[1000];
		y2 = new int[1000];
		x2[0] = 900;
		y2[0] = SCREEN_HEIGHT / 2;
		dir2 = 1;
		length2 = 1;
		hp2 = 100;
		maxHp2 = 100;
		hpBar2 = new ProgressBar(1);
		hpBar2.setPrefSize(350, 40);
		hpBar2.relocate(640, 10);
		hpBar2.setOpacity(0.5);
		
		xFood = new int[100];
		yFood = new int[100];
		nFood = 0;
		
		addTail(3);
		addTail2(3);
		addFood(30);
		root.getChildren().addAll(canvasFood, canvas, canvas2, result, hpBar, hpBar2);
		
		timer = new Timeline(new KeyFrame(Duration.millis(1000 / FRAME_RATE), e -> {
			check = false;
			check2 = false;
			update();
		}));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
		
		Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		addListener(scene);
		
		stage.setTitle("Snake");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		stage.setOnCloseRequest(event -> { timer.stop(); });
	}
	
	private void addTail(int l) {
		for (int i = 0; i < l; ++i) {
			x[length] = x[length - 1];
			y[length] = y[length - 1];
			length ++;
		}
	}
	
	private void addTail2(int l) {
		for (int i = 0; i < l; ++i) {
			x2[length2] = x2[length2 - 1];
			y2[length2] = y2[length2 - 1];
			length2 ++;
		}
	}

	private void update() {
		move();
		draw();
		checkBound();
		checkDead();
	}
	
	private void addFood(int f) {
		for (int i = 0; i < f; ++i) {
			xFood[nFood] = new Random().nextInt(SCREEN_WIDTH / GRID_SIZE) * GRID_SIZE;
			yFood[nFood++] = new Random().nextInt(SCREEN_HEIGHT / GRID_SIZE) * GRID_SIZE;
		}
	}
	
	private void move() {
		for (int i = length - 1; i > 0; --i) {
			if (x[i] != x[i-1] || y[i] != y[i-1]) {
				x[i] = x[i-1];
				y[i] = y[i-1];
			}
		}
		
		for (int i = length2 - 1; i > 0; --i) {
			if (x2[i] != x2[i-1] || y2[i] != y2[i-1]) {
				x2[i] = x2[i-1];
				y2[i] = y2[i-1];
			}
		}
		
		for (int i = 0; i < nFood; ++i) {
			if (x[0] == xFood[i] && y[0] == yFood[i]) {
				addTail(2);
				xFood[i] = new Random().nextInt(SCREEN_WIDTH / GRID_SIZE) * GRID_SIZE;
				yFood[i] = new Random().nextInt(SCREEN_HEIGHT / GRID_SIZE) * GRID_SIZE;
			}
		}
		
		for (int i = 0; i < nFood; ++i) {
			if (x2[0] == xFood[i] && y2[0] == yFood[i]) {
				addTail2(2);
				xFood[i] = new Random().nextInt(SCREEN_WIDTH / GRID_SIZE) * GRID_SIZE;
				yFood[i] = new Random().nextInt(SCREEN_HEIGHT / GRID_SIZE) * GRID_SIZE;
			}
		}
		
		if (dir == 1) {
			y[0] -= GRID_SIZE;
		} else if (dir == 2) {
			x[0] -= GRID_SIZE;
		} else if (dir == 3) {
			y[0] += GRID_SIZE;
		} else if (dir == 4) {
			x[0] += GRID_SIZE;
		}
		
		if (dir2 == 1) {
			y2[0] -= GRID_SIZE;
		} else if (dir2 == 2) {
			x2[0] -= GRID_SIZE;
		} else if (dir2 == 3) {
			y2[0] += GRID_SIZE;
		} else if (dir2 == 4) {
			x2[0] += GRID_SIZE;
		}
	}

	private void draw() {
		GraphicsContext gc = canvasFood.getGraphicsContext2D();
		GraphicsContext gc2 = canvasFood.getGraphicsContext2D();
		GraphicsContext gcF = canvasFood.getGraphicsContext2D();
		gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gc2.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		gcF.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		gc.setFill(Color.CADETBLUE);
		for (int i = 0; i < length; ++i)
			gc.fillRect(x[i], y[i], GRID_SIZE, GRID_SIZE);
		
		gc2.setFill(Color.INDIANRED);
		for (int i = 0; i < length2; ++i)
			gc2.fillRect(x2[i], y2[i], GRID_SIZE, GRID_SIZE);
		
		gcF.setFill(Color.GOLDENROD);
		for (int i = 0; i < nFood; ++i)
			gcF.fillRect(xFood[i], yFood[i], GRID_SIZE, GRID_SIZE);
	}
	
	private void checkBound() {
		if (x[0] < 0)
			x[0] = SCREEN_WIDTH - GRID_SIZE;
		else if (x[0] >= SCREEN_WIDTH)
			x[0] = 0;
		if (y[0] < 0)
			y[0] = SCREEN_HEIGHT - GRID_SIZE;
		else if (y[0] >= SCREEN_HEIGHT)
			y[0] = 0;
		
		if (x2[0] < 0)
			x2[0] = SCREEN_WIDTH - GRID_SIZE;
		else if (x2[0] >= SCREEN_WIDTH)
			x2[0] = 0;
		if (y2[0] < 0)
			y2[0] = SCREEN_HEIGHT - GRID_SIZE;
		else if (y2[0] >= SCREEN_HEIGHT)
			y2[0] = 0;
	}
	
	private void checkDead() {
		for (int i = length - 1; i > 0; --i) {
			if (x[0] == x[i] && y[0] == y[i]) {
				hp -= 20;
			}
			if (x2[0] == x[i] && y2[0] == y[i]) {
				hp2 -= 20;
			}
		}
		
		for (int i = length2 - 1; i > 0; --i) {
			if (x2[0] == x2[i] && y2[0] == y2[i]) {
				hp2 -= 20;
			}
			if (x[0] == x2[i] && y[0] == y2[i]) {
				hp -= 20;
			}
		}
		
		hpBar.setProgress(hp / maxHp);
		hpBar2.setProgress(hp2 / maxHp2);
		
		if (hp <= 0)
			die(2);
		if (hp2 <= 0)
			die(1);
	}
	
	private void die(int d) {
		timer.stop();
		canvas.setOpacity(0.5);
		result.setText("Player " + d + " wins");
		result.setVisible(true);
	}

	private void addListener(Scene scene) {
		scene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.W && dir != 3 && !check) {
				dir = 1;
				check = true;
			} else if (e.getCode() == KeyCode.A && dir != 4 && !check) {
				dir = 2;
				check = true;
			} else if (e.getCode() == KeyCode.S && dir != 1 && !check) {
				dir = 3;
				check = true;
			} else if (e.getCode() == KeyCode.D && dir != 2 && !check) {
				dir = 4;
				check = true;
			} else if (e.getCode() == KeyCode.UP && dir2 != 3 && !check2) {
				dir2 = 1;
				check2 = true;
			} else if (e.getCode() == KeyCode.LEFT && dir2 != 4 && !check2) {
				dir2 = 2;
				check2 = true;
			} else if (e.getCode() == KeyCode.DOWN && dir2 != 1 && !check2) {
				dir2 = 3;
				check2 = true;
			} else if (e.getCode() == KeyCode.RIGHT && dir2 != 2 && !check2) {
				dir2 = 4;
				check2 = true;
			} else if (e.getCode() == KeyCode.SPACE) {
				if (timer.getStatus() == Animation.Status.PAUSED)
					timer.play();
				else
					timer.pause();
			} else if (e.getCode() == KeyCode.ENTER && timer.getStatus() == Animation.Status.STOPPED) {
				x[0] = 100;
				y[0] = SCREEN_HEIGHT / 2;
				dir = 1;
				length = 1;
				hp = 100;
				
				x2[0] = 900;
				y2[0] = SCREEN_HEIGHT / 2;
				dir2 = 1;
				length2 = 1;
				hp2 = 100;
				
				addTail(3);
				addTail2(3);
				
				canvasFood.setOpacity(1);
				result.setVisible(false);
				
				timer.play();
			}
		});
	}
	
	
	
}
