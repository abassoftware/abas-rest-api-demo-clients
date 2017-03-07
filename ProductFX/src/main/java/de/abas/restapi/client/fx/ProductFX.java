package de.abas.restapi.client.fx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.core.Response;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import de.abas.restapi.client.ClientCreator;
import de.abas.restapi.client.ConnectionData;
import de.abas.restapi.client.I18N;
import de.abas.restapi.client.filters.EdpLoggingFilter;
import de.abas.restapi.client.filters.LanguageFilter;
import de.abas.restapi.client.model.ProductPart;
import de.abas.restapi.client.rest.IFileServer;
import de.abas.restapi.client.rest.ProductParts;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ProductFX extends Application {
	
	private static final boolean SHOW_GRID = false;

	private final double W = 1400;
	private final double H = 800;
	private Stage stage;

	private TextField textPartInput;
	private String currentProductIdno;
	private String currentProductBarcodeFilePath;
	private String imageName;
	private HBox imageBox;

	private Label labelStatus;
 
	// Main information
	private TextField textfieldIdno;
	private TextField textfieldSwd;
	private TextField textfieldDescr;
	private TextField textfieldStock;
	private Label labelStockUnit;
	private TextField textfieldProcureMode;
	private TextArea textareaSalesDescr;
	private TextField textfieldSalesPrice;
	private Label labelSalesPriceCurr;

	private HBox hboxProductImage;
	private ImageView imageviewProduct;

	private static final boolean EDP_LOGGING_ON = false;
	private static ResteasyClient client;
	private static ResteasyWebTarget target;
	
	//private static final Log LOG = LogFactory.getLog(ProductFX.class);
	private static final  Logger logger = Logger.getLogger("ProductFX");
	
	public static void main(String[] args) {
		System.setProperty("http.nonProxyHosts", "*.abasag.intra");
		
		Handler fh = null;
		try {
			fh = new FileHandler("fx.log", true);
			fh.setLevel (Level.INFO);
			fh.setFormatter(new SimpleFormatter());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.addHandler(fh);
		
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(ConnectionData.BASIC_AUTH_USERNAME,
				ConnectionData.BASIC_AUTH_PASSWORD);
		client = ClientCreator.createClientWithBasicAuthentication(ConnectionData.HOST, ConnectionData.PORT,
				credentials);
		target = client.target("http://" + ConnectionData.HOST + ":" + ConnectionData.PORT + ConnectionData.BASE);

		addFilterForEdpLogging();
		addFilterForLanguage();

		logger.info("## ProductFX: " + "Starting");
		launch(args);
	}
	

	private static void addFilterForLanguage() {
		LanguageFilter languageFilter = new LanguageFilter();
		client.register(languageFilter);
		languageFilter.setValue(I18N.LANG);
	}

	private static void addFilterForEdpLogging() {
		if (EDP_LOGGING_ON) {
			EdpLoggingFilter filter = new EdpLoggingFilter();
			client.register(filter);
			filter.setValue("INFO");
		}
	}

	@Override
	public void start(Stage stage) {
		setUserAgentStylesheet(STYLESHEET_MODENA);
		// FlatterFX.style();
		this.stage = stage;
		Scene scene = new Scene(createGridPane(), W, H);
		//scene.getStylesheets().add("fxclient_product.css");
		//String css = ProductFX.class.getResource("fxclient_product.css").toExternalForm();
		//scene.getStylesheets().clear();
		//scene.getStylesheets().add(css);
		File css = new File("resources/fxclient_product.css");
		scene.getStylesheets().clear();
		this.stage.setScene(scene);
		scene.getStylesheets().add("file:///" + css.getAbsolutePath().replace("\\", "/"));
		
		this.stage.setTitle(".: ABAS Product Viewer :.");
		File logo = new File("resources/icon.png");
		try {
			FileInputStream fis = new FileInputStream(logo);
			this.stage.getIcons().add(new Image(fis));
		} catch (FileNotFoundException e) {
			System.out.println("Can't load the logo!");
			logger.info("## ProductFX: " + "Can't load the logo!");
		}
		this.stage.show();

	}

	private GridPane createGridPane() {
		GridPane gridPane = new GridPane();
		gridPane.setHgap(4);
		gridPane.setVgap(4);
		gridPane.setId("main");

		// Create the header bar area
		HBox topSide = createHeaderBar();
		GridPane.setConstraints(topSide, 0, 0, 2, 1, HPos.LEFT, VPos.TOP);

		// Create the main content area
		GridPane mainContent = createMainContentPane();
		GridPane.setConstraints(mainContent, 0, 1, 2, 1, HPos.LEFT, VPos.TOP);

		// Create the bottom content area
		GridPane bottomContent = createBottomContentPane();
		GridPane.setConstraints(bottomContent, 0, 2, 2, 1, HPos.LEFT, VPos.TOP);

		ColumnConstraints cc1 = new ColumnConstraints();
		cc1.setMinWidth(400.0d);
		cc1.setMaxWidth(400.0d);
		ColumnConstraints cc2 = new ColumnConstraints();
		cc2.setFillWidth(true);
		cc2.setHgrow(Priority.ALWAYS);

		RowConstraints rc1 = new RowConstraints();
		rc1.setMinHeight(100.0d);
		RowConstraints rc2 = new RowConstraints();
		rc2.setFillHeight(true);
		rc2.setVgrow(Priority.ALWAYS);
		RowConstraints rc3 = new RowConstraints();
		rc3.setMinHeight(50.0d);
		rc3.setMaxHeight(50.0d);
		rc3.setVgrow(Priority.ALWAYS);

		gridPane.getColumnConstraints().addAll(cc1, cc2);
		gridPane.getRowConstraints().addAll(rc1, rc2, rc3);

		gridPane.getChildren().addAll(topSide, mainContent, bottomContent);
		gridPane.setGridLinesVisible(SHOW_GRID);

		return gridPane;
	}

	private HBox createHeaderBar() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setId("headerContent");

		ImageView imageBarcode = new ImageView();

		this.textPartInput = new TextField();
		this.textPartInput.setId("headerTextfield");

		this.textPartInput.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.TAB)) {
					currentProductIdno = textPartInput.getText();

					if (!currentProductIdno.trim().equals("")) {
						try {
							createBarcode();
							FileInputStream fis = new FileInputStream(currentProductBarcodeFilePath);
							imageBarcode.setImage(new Image(fis));
							imageBox.setVisible(true);

							// long s = System.currentTimeMillis();
							performRestCallAndFillFields();
							// long end = System.currentTimeMillis();
							// System.out.println("RUNTIME: " + (end-s));
						} catch (FileNotFoundException exception) {
							System.out.println(
									"Exception: ProductFX.textPartInput.handle(): Can't find the image file for the product.");
							logger.info("## ProductFX: " + "Exception: ProductFX.textPartInput.handle(): Can't find the image file for the product.");
						}
					}

					textPartInput.setText("");
					textPartInput.requestFocus();
				}
			}

		});

		this.imageBox = new HBox();
		imageBox.getChildren().add(imageBarcode);
		imageBox.setId("headerImageBarcode");
		this.imageBox.setVisible(false);

		hbox.getChildren().add(textPartInput);
		hbox.getChildren().add(imageBox);
		return hbox;
	}

	private void createBarcode() {
		Code128Bean bean = new Code128Bean();
		final int dpi = 100;
		bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
		bean.setModuleWidth(0.4);
		bean.doQuietZone(false);
		// Open output file
		try {
			File outputFile = new File("barcodes/product_barcode.png");
			OutputStream out = new FileOutputStream(outputFile);

			// Set up the canvas provider for monochrome PNG output
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/x-png", dpi,
					BufferedImage.TYPE_BYTE_BINARY, false, 0);

			// Generate the barcode
			bean.generateBarcode(canvas, currentProductIdno);
			currentProductBarcodeFilePath = outputFile.getPath();

			// Signal end of generation
			canvas.finish();
			out.close();
		} catch (Exception e) {
			logger.info("## ProductFX: " + "Exception: Can't create barcode.");
		}

	}

	private GridPane createMainContentPane() {
		GridPane mainContent = new GridPane();
		mainContent.setPadding(new Insets(15, 12, 15, 12));
		mainContent.setHgap(16);
		mainContent.setVgap(16);
		mainContent.setId("mainContent");
		mainContent.setGridLinesVisible(SHOW_GRID);

		Label labelIdno = new Label("Idno");
		this.textfieldIdno = new TextField();

		Label labelSwd = new Label("Searchword");
		this.textfieldSwd = new TextField();

		Label labelDescr = new Label("Description");
		this.textfieldDescr = new TextField();

		Label labelStock = new Label("Stock");
		this.textfieldStock = new TextField();
		this.labelStockUnit = new Label("Piece");

		Label labelProcureMode = new Label("Procure mode");
		this.textfieldProcureMode = new TextField();

		Label labelSalesDescr = new Label("Sales description");
		this.textareaSalesDescr = new TextArea();

		Label labelSalesPrice = new Label("Price");
		this.textfieldSalesPrice = new TextField();
		this.labelSalesPriceCurr = new Label("EUR");

		this.hboxProductImage = new HBox();
		this.imageviewProduct = new ImageView();
		this.imageviewProduct.setFitWidth(400.0d);
		this.imageviewProduct.setFitHeight(500.0d);
		this.imageviewProduct.setPreserveRatio(true);
		this.hboxProductImage.setId("imageProductBox");
		this.hboxProductImage.getChildren().add(imageviewProduct);

		// Main information
		GridPane.setConstraints(labelIdno, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textfieldIdno, 1, 0, 1, 1, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(labelSwd, 2, 0, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textfieldSwd, 3, 0, 1, 1, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(labelDescr, 0, 1, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textfieldDescr, 1, 1, 3, 1, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(labelStock, 0, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textfieldStock, 1, 2, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(labelStockUnit, 2, 2, 1, 1, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(labelProcureMode, 0, 3, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textfieldProcureMode, 1, 3, 3, 1, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(labelSalesPrice, 0, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textfieldSalesPrice, 1, 4, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(labelSalesPriceCurr, 2, 4, 1, 1, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(labelSalesDescr, 0, 5, 1, 1, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(textareaSalesDescr, 1, 5, 3, 3, HPos.LEFT, VPos.CENTER);

		GridPane.setConstraints(hboxProductImage, 4, 0, 1, 6, HPos.RIGHT, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);

		mainContent.getChildren().addAll(labelIdno, textfieldIdno, labelSwd, textfieldSwd, labelDescr, textfieldDescr,
				labelStock, textfieldStock, labelStockUnit, labelProcureMode, textfieldProcureMode, labelSalesDescr,
				textareaSalesDescr, labelSalesPriceCurr, hboxProductImage, labelSalesPrice, textfieldSalesPrice);

		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setFillWidth(true);
		columnConstraints.setHgrow(Priority.ALWAYS);
		mainContent.getColumnConstraints().add(columnConstraints);

		disableInputFields();

		return mainContent;
	}


	private GridPane createBottomContentPane() {
		GridPane bottomContent = new GridPane();
		bottomContent.setHgap(4);
		bottomContent.setVgap(4);
		bottomContent.setId("bottomContent");

		this.labelStatus = new Label("Welcome! Please scan or manually enter your product number.");
		bottomContent.getChildren().add(this.labelStatus);

		return bottomContent;
	}

	/**
	 * Disable all the input fields
	 */
	private void disableInputFields() {
		this.textfieldIdno.setDisable(true);
		this.textfieldSwd.setDisable(true);
		this.textfieldDescr.setDisable(true);
		this.textfieldStock.setDisable(true);
		this.textfieldProcureMode.setDisable(true);
		this.textareaSalesDescr.setDisable(true);
		this.textfieldSalesPrice.setDisable(true);
	}

	private void performRestCallAndFillFields() {
		try {
			final ProductParts parts = ProxyBuilder.builder(ProductParts.class, target).build();
			logger.info("## ProductFX: " + target.getUri().toString());
			String headerFields = ProductPart.headerFields();
			logger.info("## ProductFX: " + "Using header fields: " + headerFields);
			String data = parts.withId(currentProductIdno, headerFields, I18N.LANG);
			System.out.println(data);
			logger.info("## ProductFX: " + data);
			
			ProductPart part = ProductPart.forData(data);
			logger.info("## ProductFX: " + "Got a part");

			this.imageName = "";
			if (I18N.LANG.equalsIgnoreCase("EN")) {
				this.textfieldIdno.setText(part.get(ProductPart.HEAD_FIELDS_EN[0]));
				this.textfieldSwd.setText(part.get(ProductPart.HEAD_FIELDS_EN[1]));
				this.textfieldDescr.setText(part.get(ProductPart.HEAD_FIELDS_EN[2]));
				this.textfieldStock.setText(part.get(ProductPart.HEAD_FIELDS_EN[3]));
				this.labelStockUnit.setText(part.get(ProductPart.HEAD_FIELDS_EN[4]));
				this.textfieldProcureMode.setText(part.get(ProductPart.HEAD_FIELDS_EN[5]));
				this.textareaSalesDescr.setText(part.get(ProductPart.HEAD_FIELDS_EN[6]));
				this.textfieldSalesPrice.setText(part.get(ProductPart.HEAD_FIELDS_EN[7]));
				this.labelSalesPriceCurr.setText(part.get(ProductPart.HEAD_FIELDS_EN[8]));
				this.imageName = part.get(ProductPart.HEAD_FIELDS_EN[9]);
			} else {
				this.textfieldIdno.setText(part.get(ProductPart.HEAD_FIELDS_DE[0]));
				this.textfieldSwd.setText(part.get(ProductPart.HEAD_FIELDS_DE[1]));
				this.textfieldDescr.setText(part.get(ProductPart.HEAD_FIELDS_DE[2]));
				this.textfieldStock.setText(part.get(ProductPart.HEAD_FIELDS_DE[3]));
				this.labelStockUnit.setText(part.get(ProductPart.HEAD_FIELDS_DE[4]));
				this.textfieldProcureMode.setText(part.get(ProductPart.HEAD_FIELDS_DE[5]));
				this.textareaSalesDescr.setText(part.get(ProductPart.HEAD_FIELDS_DE[6]));
				this.textfieldSalesPrice.setText(part.get(ProductPart.HEAD_FIELDS_DE[7]));
				this.labelSalesPriceCurr.setText(part.get(ProductPart.HEAD_FIELDS_DE[8]));
				this.imageName = part.get(ProductPart.HEAD_FIELDS_DE[9]);
			}

			System.out.println("Found: " + imageName);
			logger.info("## ProductFX: " + "Found: " + imageName);
			if (imageName != null && !imageName.trim().equals("")) {
				IFileServer fs = ProxyBuilder.builder(IFileServer.class, target).build();
				Response fileInPngFormat = fs.getFileInPngFormat(imageName);
				saveFile(fileInPngFormat);
				fileInPngFormat.close();
			}

			String filepath = "products/" + currentProductIdno + ".png";
			File f = new File(filepath);
			if (f.exists()) {
				FileInputStream fis = new FileInputStream(filepath);
				this.imageviewProduct.setImage(new Image(fis));
			} else {
				FileInputStream fis = new FileInputStream("products/noimage.png");
				this.imageviewProduct.setImage(new Image(fis));
			}

			setStatus("Your current product is: " + currentProductIdno + " (" + this.textfieldDescr.getText() + ")");
		} catch (Exception e) {
			setStatus("The product with the id \"" + currentProductIdno + "\" does not exist");
			logger.info("## ProductFX: " + "The product with the id \"" + currentProductIdno + "\" does not exist");
			this.imageBox.setVisible(false);
		}

	}

	// save uploaded file to a defined location on the server
	private void saveFile(Response response) {
		InputStream uploadedInputStream = response.readEntity(InputStream.class);
		String serverLocation = "products/" + currentProductIdno + ".png";

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
			uploadedInputStream.close();
		} catch (IOException e) {
			System.out.println("Exception: ProductFX.saveFile()");
			logger.info("## ProductFX: " + "Exception: ProductFX.saveFile()");
		}
	}

	/**
	 * Set the status text in the bottom.
	 * 
	 * @param status
	 *            The new status text
	 */
	private void setStatus(String status) {
		this.labelStatus.setText(status);
	}

}
