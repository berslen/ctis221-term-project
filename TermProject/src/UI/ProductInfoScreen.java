package UI;

import Products.Digital.DigitalBook;
import Products.Digital.DigitalGame;
import Products.Digital.DigitalMovie;
import Products.Digital.DigitalMusic;
import Products.IProduct;
import Products.Infos.BookInfo;
import Products.Infos.GameInfo;
import Products.Infos.MovieInfo;
import Products.Infos.MusicInfo;
import Products.Interfaces.IBook;
import Products.Interfaces.IGame;
import Products.Interfaces.IMovie;
import Products.Interfaces.IMusic;
import Products.MediumType;
import Products.Person;
import Products.Physical.PhysicalBook;
import Products.Physical.PhysicalGame;
import Products.Physical.PhysicalMovie;
import Products.Physical.PhysicalMusic;
import Products.ProductInfo;
import Products.ProductSystem;
import Products.ProductType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

public class ProductInfoScreen extends javax.swing.JFrame {

    private final ArrayList<JTextComponent> allInputs;

    private IProduct currentProduct;
    private MediumType currentMediumType;
    private ProductType currentProductType;

    public ProductInfoScreen() {
        initComponents();

        this.allInputs = new ArrayList<>(Arrays.asList(
                nameOutput,
                relasedateOutput,
                genreOutput,
                languageOutput,
                publisherOutput,
                priceOutput,
                bookIsbnOutput,
                bookAuthorOutput,
                bookPageCountOutput,
                gameDevOutput,
                gameEngineOutput,
                gamePlatformsOutput,
                movieBoxOfficeOutput,
                movieCountryOutput,
                movieDirectorOutput,
                movieLengthOutput,
                movieProducerOutput,
                musicAlbumOutput,
                musicArtistOutput,
                musicLengthOutput,
                musicProducerOutput,
                // this one is a textArea
                movieStarringOutput
        ));
    }

    public void setUpForViewOrEdit(IProduct product, boolean canEdit) {
        clear();

        this.currentProduct = product;
        this.currentMediumType = product.getMediumType();
        this.currentProductType = product.getProductType();

        fillGenericPanel(product.getProductInfo());
        fillSpecificPanel(product.getProductType(), product.getProductInfo());
        showSpecificPanel(product.getProductType());

        setTitle(canEdit ? "Edit Product" : "View Product");

        addButton.setVisible(false);
        updateButton.setVisible(canEdit);

        for (JComponent it : allInputs) {
            it.setEnabled(canEdit);
        }
    }

    public void setUpForAdd(MediumType mediumType, ProductType productType) {
        clear();

        this.currentProduct = null;
        this.currentMediumType = mediumType;
        this.currentProductType = productType;

        showSpecificPanel(productType);

        setTitle("Add a new Product");

        addButton.setVisible(true);
        updateButton.setVisible(false);

        for (JComponent it : allInputs) {
            it.setEnabled(true);
        }
    }

    private void clear() {
        for (JTextComponent it : allInputs) {
            it.setText("");
        }
    }

    private void fillGenericPanel(ProductInfo info) {
        nameOutput.setText(info.getProductName());
        relasedateOutput.setText(info.getReleaseDate());
        genreOutput.setText(info.getGenre());
        languageOutput.setText(info.getLanguage());
        publisherOutput.setText(info.getPublisher());
        priceOutput.setText(Double.toString(info.getPrice()));
    }

    private void fillSpecificPanel(ProductType productType, ProductInfo info) {
        switch (productType) {
            case Book:
                fillSpecific((BookInfo) info);
                break;

            case Game:
                fillSpecific((GameInfo) info);
                break;

            case Movie:
                fillSpecific((MovieInfo) info);
                break;

            case Music:
                fillSpecific((MusicInfo) info);
                break;
        }
    }

    private void showSpecificPanel(ProductType type) {
        bookPanel.setVisible(false);
        gamePanel.setVisible(false);
        moviePanel.setVisible(false);
        musicPanel.setVisible(false);

        switch (type) {
            case Book:
                bookPanel.setVisible(true);
                break;

            case Game:
                gamePanel.setVisible(true);
                break;

            case Movie:
                moviePanel.setVisible(true);
                break;

            case Music:
                musicPanel.setVisible(true);
                break;
        }
    }

    private void fillSpecific(BookInfo info) {
        bookIsbnOutput.setText(Integer.toString(info.getIsbn()));
        bookAuthorOutput.setText(info.getAuthor().getFullName());
        bookPageCountOutput.setText(Integer.toString(info.getPageCount()));
    }

    private void fillSpecific(GameInfo info) {
        gameDevOutput.setText(info.getDeveloper().getFullName());
        gameEngineOutput.setText(info.getEngine());
        gamePlatformsOutput.setText(info.getPlatforms());
    }

    private void fillSpecific(MovieInfo info) {
        movieBoxOfficeOutput.setText(Double.toString(info.getBoxOffice()));
        movieCountryOutput.setText(info.getCountry());
        movieDirectorOutput.setText(info.getDirector().getFullName());
        movieLengthOutput.setText(info.getLength());
        movieProducerOutput.setText(info.getProducer());

        ArrayList<Person> starring = info.getStarring();

        movieStarringOutput.setText("");

        for (Person it : starring) {
            movieStarringOutput.append(it.getFullName() + "\r\n");
        }
    }

    private void fillSpecific(MusicInfo info) {
        musicAlbumOutput.setText(info.getAlbum());
        musicArtistOutput.setText(info.getArtist().getFullName());
        musicLengthOutput.setText(info.getLength());
    }

    private IMusic createNewMusic() {
        MusicInfo info = collateMusicInfo();

        switch (currentMediumType) {
            case Digital:
                return new DigitalMusic(info);

            case Physical:
                return new PhysicalMusic(info);
        }

        return null;
    }

    private IMovie createNewMovie() {
        MovieInfo info = collateMovieInfo();

        switch (currentMediumType) {
            case Digital:
                return new DigitalMovie(info);

            case Physical:
                return new PhysicalMovie(info);
        }

        return null;
    }

    private IGame createNewGame() {
        GameInfo info = collateGameInfo();

        switch (currentMediumType) {
            case Digital:
                return new DigitalGame(info);

            case Physical:
                return new PhysicalGame(info);
        }

        return null;
    }

    private IBook createNewBook() {
        BookInfo info = collateBookInfo();

        switch (currentMediumType) {
            case Digital:
                return new DigitalBook(info);

            case Physical:
                return new PhysicalBook(info);
        }

        return null;
    }

    private MusicInfo collateMusicInfo() {
        // specific
        String album = musicAlbumOutput.getText();

        String[] artistNames = musicArtistOutput.getText().split(" ");
        Person artist = new Person(artistNames[0], artistNames[1]);

        String length = musicLengthOutput.getText();
        String producer = musicProducerOutput.getText();

        // generic
        String releaseDate = relasedateOutput.getText();
        double price = Double.parseDouble(priceOutput.getText());
        String productName = nameOutput.getText();
        String publisher = publisherOutput.getText();
        String genre = genreOutput.getText();
        String language = languageOutput.getText();

        return new MusicInfo(album, artist, length, producer,
                releaseDate, price, productName, publisher, genre, language);
    }

    private MovieInfo collateMovieInfo() {
        // String producer, Person director, String country, ArrayList<Person> starring, int length, double boxOffice,

        // specific
        String producer = movieProducerOutput.getText();

        String[] directorNames = movieDirectorOutput.getText().split(" ");
        Person director = new Person(directorNames[0], directorNames[1]);

        String country = movieCountryOutput.getText();
        String length = movieLengthOutput.getText();
        double boxOffice = Double.parseDouble(movieBoxOfficeOutput.getText());

        // generic
        String releaseDate = relasedateOutput.getText();
        double price = Double.parseDouble(priceOutput.getText());
        String productName = nameOutput.getText();
        String publisher = publisherOutput.getText();
        String genre = genreOutput.getText();
        String language = languageOutput.getText();

        return new MovieInfo(producer, director, country, parseMovieStarring(), length, boxOffice,
                releaseDate, price, productName, publisher, genre, language);
    }

    private ArrayList<Person> parseMovieStarring() {
        ArrayList<Person> result = new ArrayList<>();

        Scanner sc = new Scanner(movieStarringOutput.getText());

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] split = line.split(" ");
            result.add(new Person(split[0], split[1]));
        }

        return result;
    }

    private GameInfo collateGameInfo() {
        // specific
        String platforms = gamePlatformsOutput.getText();

        String[] developerNames = gameDevOutput.getText().split(" ");
        Person developer = new Person(developerNames[0], developerNames[1]);

        String engine = gameEngineOutput.getText();

        // generic
        String releaseDate = relasedateOutput.getText();
        double price = Double.parseDouble(priceOutput.getText());
        String productName = nameOutput.getText();
        String publisher = publisherOutput.getText();
        String genre = genreOutput.getText();
        String language = languageOutput.getText();

        return new GameInfo(platforms, engine, developer,
                releaseDate, price, productName, publisher, genre, language);
    }

    private BookInfo collateBookInfo() {
        // int pageCount, int isbn, Person author,

        // specific
        int pageCount = Integer.parseInt(bookPageCountOutput.getText());

        int isbn = Integer.parseInt(bookIsbnOutput.getText());

        String[] authorNames = bookAuthorOutput.getText().split(" ");
        Person author = new Person(authorNames[0], authorNames[1]);

        // generic
        String releaseDate = relasedateOutput.getText();
        double price = Double.parseDouble(priceOutput.getText());
        String productName = nameOutput.getText();
        String publisher = publisherOutput.getText();
        String genre = genreOutput.getText();
        String language = languageOutput.getText();

        return new BookInfo(pageCount, isbn, author,
                releaseDate, price, productName, publisher, genre, language);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        genericPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        priceOutput = new javax.swing.JTextField();
        publisherOutput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        languageOutput = new javax.swing.JTextField();
        genreOutput = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        relasedateOutput = new javax.swing.JTextField();
        nameOutput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        specificPanelsHolder = new javax.swing.JPanel();
        bookPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        bookAuthorOutput = new javax.swing.JTextField();
        bookIsbnOutput = new javax.swing.JTextField();
        bookPageCountOutput = new javax.swing.JTextField();
        gamePanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        gamePlatformsOutput = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        gameEngineOutput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        gameDevOutput = new javax.swing.JTextField();
        moviePanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        movieProducerOutput = new javax.swing.JTextField();
        movieDirectorOutput = new javax.swing.JTextField();
        movieCountryOutput = new javax.swing.JTextField();
        movieLengthOutput = new javax.swing.JTextField();
        movieBoxOfficeOutput = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        movieStarringOutput = new javax.swing.JTextArea();
        musicPanel = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        musicArtistOutput = new javax.swing.JTextField();
        musicLengthOutput = new javax.swing.JTextField();
        musicAlbumOutput = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        musicProducerOutput = new javax.swing.JTextField();
        buttonsPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(724, 375));
        setResizable(false);

        jLabel5.setText("Price");

        priceOutput.setEnabled(false);

        publisherOutput.setEnabled(false);

        jLabel6.setText("Publisher");

        jLabel4.setText("Language");

        languageOutput.setEnabled(false);
        languageOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageOutputActionPerformed(evt);
            }
        });

        genreOutput.setEnabled(false);

        jLabel3.setText("Genre");

        jLabel2.setText("Release Date");

        relasedateOutput.setEnabled(false);

        nameOutput.setEnabled(false);

        jLabel1.setText("Name");

        javax.swing.GroupLayout genericPanelLayout = new javax.swing.GroupLayout(genericPanel);
        genericPanel.setLayout(genericPanelLayout);
        genericPanelLayout.setHorizontalGroup(
            genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(genericPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(relasedateOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genreOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(languageOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(publisherOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        genericPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {genreOutput, languageOutput, nameOutput, priceOutput, publisherOutput, relasedateOutput});

        genericPanelLayout.setVerticalGroup(
            genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(genericPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nameOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(relasedateOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(genreOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(languageOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(publisherOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(genericPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(priceOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        specificPanelsHolder.setLayout(new java.awt.CardLayout());

        jLabel10.setText("Author");

        jLabel11.setText("ISBN");

        jLabel12.setText("Page Count");

        bookAuthorOutput.setEnabled(false);

        bookIsbnOutput.setEnabled(false);

        bookPageCountOutput.setEnabled(false);

        javax.swing.GroupLayout bookPanelLayout = new javax.swing.GroupLayout(bookPanel);
        bookPanel.setLayout(bookPanelLayout);
        bookPanelLayout.setHorizontalGroup(
            bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bookPageCountOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookAuthorOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bookIsbnOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bookPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel12});

        bookPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bookAuthorOutput, bookIsbnOutput, bookPageCountOutput});

        bookPanelLayout.setVerticalGroup(
            bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bookIsbnOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(bookAuthorOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(bookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(bookPageCountOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        specificPanelsHolder.add(bookPanel, "card2");

        jLabel7.setText("Platforms");

        gamePlatformsOutput.setEnabled(false);

        jLabel8.setText("Engine");

        gameEngineOutput.setEnabled(false);

        jLabel9.setText("Developer");

        gameDevOutput.setEnabled(false);

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gamePanelLayout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(gameDevOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(gamePanelLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(gamePlatformsOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(gamePanelLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(gameEngineOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(265, 265, 265))))
        );

        gamePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {gameDevOutput, gameEngineOutput, gamePlatformsOutput});

        gamePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel7, jLabel8, jLabel9});

        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gamePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(gamePlatformsOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gameEngineOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(gameDevOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(134, Short.MAX_VALUE))
        );

        specificPanelsHolder.add(gamePanel, "card3");

        jLabel13.setText("Producer");

        jLabel14.setText("Director");

        jLabel15.setText("Country");

        jLabel17.setText("Length");

        jLabel18.setText("Box Office");

        movieProducerOutput.setEnabled(false);

        movieDirectorOutput.setEnabled(false);

        movieCountryOutput.setEnabled(false);

        movieLengthOutput.setEnabled(false);

        movieBoxOfficeOutput.setEnabled(false);

        jLabel16.setText("Starring");

        movieStarringOutput.setColumns(10);
        movieStarringOutput.setRows(5);
        movieStarringOutput.setEnabled(false);
        jScrollPane2.setViewportView(movieStarringOutput);

        javax.swing.GroupLayout moviePanelLayout = new javax.swing.GroupLayout(moviePanel);
        moviePanel.setLayout(moviePanelLayout);
        moviePanelLayout.setHorizontalGroup(
            moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moviePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(moviePanelLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(30, 30, 30)
                        .addComponent(movieProducerOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(moviePanelLayout.createSequentialGroup()
                        .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(30, 30, 30)
                        .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(movieDirectorOutput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(movieCountryOutput)))
                    .addGroup(moviePanelLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(30, 30, 30)
                        .addComponent(movieLengthOutput))
                    .addGroup(moviePanelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(30, 30, 30)
                        .addComponent(movieBoxOfficeOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(56, 56, 56)
                .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        moviePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel13, jLabel14, jLabel15, jLabel16, jLabel17, jLabel18});

        moviePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {movieBoxOfficeOutput, movieCountryOutput, movieDirectorOutput, movieLengthOutput, movieProducerOutput});

        moviePanelLayout.setVerticalGroup(
            moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(moviePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(movieProducerOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(18, 18, 18)
                .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(moviePanelLayout.createSequentialGroup()
                        .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(movieDirectorOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(movieCountryOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(movieLengthOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(moviePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(movieBoxOfficeOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)))
                    .addComponent(jScrollPane2))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        specificPanelsHolder.add(moviePanel, "card4");

        jLabel19.setText("Artist");

        jLabel20.setText("Length");

        jLabel21.setText("Album");

        musicArtistOutput.setEnabled(false);

        musicLengthOutput.setEnabled(false);

        musicAlbumOutput.setEnabled(false);

        jLabel22.setText("Producer");

        musicProducerOutput.setEnabled(false);

        javax.swing.GroupLayout musicPanelLayout = new javax.swing.GroupLayout(musicPanel);
        musicPanel.setLayout(musicPanelLayout);
        musicPanelLayout.setHorizontalGroup(
            musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(musicPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(musicAlbumOutput)
                    .addComponent(musicProducerOutput)
                    .addComponent(musicArtistOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(musicLengthOutput))
                .addContainerGap(271, Short.MAX_VALUE))
        );

        musicPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel19, jLabel20, jLabel21});

        musicPanelLayout.setVerticalGroup(
            musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(musicPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(musicArtistOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(musicAlbumOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(musicProducerOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(musicPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(musicLengthOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );

        specificPanelsHolder.add(musicPanel, "card5");

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10);
        flowLayout1.setAlignOnBaseline(true);
        buttonsPanel.setLayout(flowLayout1);

        addButton.setText("Add the Product");
        addButton.setPreferredSize(new java.awt.Dimension(127, 23));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(addButton);

        updateButton.setText("Update the Product");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(updateButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(genericPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(specificPanelsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(genericPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(specificPanelsHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        try {
            IProduct newProduct = null;
            
            switch (currentProductType) {
                case Book:
                    newProduct = createNewBook();
                    break;

                case Game:
                    newProduct = createNewGame();
                    break;

                case Movie:
                    newProduct = createNewMovie();
                    break;

                case Music:
                    newProduct = createNewMusic();
                    break;
            }

            if (newProduct == null) {
                JOptionPane.showMessageDialog(null, "Could not create the product.");
                return;
            }

            ProductSystem.addProduct(newProduct);

            ProductSystem.saveAllToFile();
            JOptionPane.showMessageDialog(null, "Successfully added the product.");
            this.dispose();
        } catch (IOException ex) {
            Logger.getLogger(ProductInfoScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        try {
            ProductInfo newProductInfo = null;

            switch (currentProductType) {
                case Book:
                    newProductInfo = collateBookInfo();
                    break;

                case Game:
                    newProductInfo = collateGameInfo();
                    break;

                case Movie:
                    newProductInfo = collateMovieInfo();
                    break;

                case Music:
                    newProductInfo = collateMusicInfo();
                    break;
            }

            this.currentProduct.updateProductInfo(newProductInfo);

            ProductSystem.saveAllToFile();
            JOptionPane.showMessageDialog(null, "Successfully edited the product.");
        } catch (IOException ex) {
            Logger.getLogger(ProductInfoScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_updateButtonActionPerformed

    private void languageOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageOutputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_languageOutputActionPerformed

//<editor-fold defaultstate="collapsed" desc="Generated code: variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTextField bookAuthorOutput;
    private javax.swing.JTextField bookIsbnOutput;
    private javax.swing.JTextField bookPageCountOutput;
    private javax.swing.JPanel bookPanel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JTextField gameDevOutput;
    private javax.swing.JTextField gameEngineOutput;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JTextField gamePlatformsOutput;
    private javax.swing.JPanel genericPanel;
    private javax.swing.JTextField genreOutput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField languageOutput;
    private javax.swing.JTextField movieBoxOfficeOutput;
    private javax.swing.JTextField movieCountryOutput;
    private javax.swing.JTextField movieDirectorOutput;
    private javax.swing.JTextField movieLengthOutput;
    private javax.swing.JPanel moviePanel;
    private javax.swing.JTextField movieProducerOutput;
    private javax.swing.JTextArea movieStarringOutput;
    private javax.swing.JTextField musicAlbumOutput;
    private javax.swing.JTextField musicArtistOutput;
    private javax.swing.JTextField musicLengthOutput;
    private javax.swing.JPanel musicPanel;
    private javax.swing.JTextField musicProducerOutput;
    private javax.swing.JTextField nameOutput;
    private javax.swing.JTextField priceOutput;
    private javax.swing.JTextField publisherOutput;
    private javax.swing.JTextField relasedateOutput;
    private javax.swing.JPanel specificPanelsHolder;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
