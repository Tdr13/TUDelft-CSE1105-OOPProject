package commons;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long boardId;
    private String title;
    @JsonManagedReference
    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Listing> lists;
    private String accessKey;
    private String password;

    @JsonManagedReference
    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Tag> tags;

    private String backgroundColorDefault="#ffffff";
    private String backgroundColor="#ffffff";
    private String textColorDefault="#000000";
    private String textColor="#000000";

    private String listBackgroundColorDefault="#ffffff";
    private String listBackgroundColor="#ffffff";
    private String listTextColorDefault="#000000";
    private String listTextColor="#000000";

    private String cardFontColor;
    private String cardBackgroundColor;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "board",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ColorScheme> schemes;


    /**
     * Constructor for a Board item.
     *
     * @param title     - title of the board
     * @param accessKey - unique accessKey for the board
     * @param password  - password of the board
     */
    public Board(String title, String accessKey, String password) {
        this.title = title;
        this.accessKey = accessKey;
        this.password = password;
        this.lists = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.schemes = new ArrayList<>();

        backgroundColorDefault="#ffffff";
        backgroundColor="#ffffff";
        textColorDefault="#000000";
        textColor="#000000";

        listBackgroundColorDefault="#ffffff";
        listBackgroundColor="#ffffff";
        listTextColorDefault="#000000";
        listTextColor="#000000";

        cardFontColor = "#000000";
        cardBackgroundColor = "#ffffff";
    }

    /**
     * Default constructor for a Board object.
     */
    @SuppressWarnings("unused")
    public Board() {
        // for object mapper
    }

    /**
     * Getter for the board id.
     *
     * @return - this board's id
     */
    public long getBoardId() {
        return boardId;
    }

    /**
     * Setter for the board's id.
     *
     * @param boardId the new ID of a board
     */
    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    /**
     * Getter for the board title.
     *
     * @return - this board's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for this board's title.
     *
     * @param title - new title of the board
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the board lists.
     *
     * @return - this board's lists
     */
    public List<Listing> getLists() {
        return lists;
    }

    /**
     * Setter for this board's lists.
     *
     * @param lists - new lists in the board
     */
    public void setLists(List<Listing> lists) {
        this.lists = lists;
    }

    /**
     * Getter for the accessKey of the board.
     *
     * @return - this board's accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * Setter for this board's accessKey, which takes the id and adds to it random characters until it gets to 10 characters overall.
     *
     */
    public void setAccessKey() {
        String result = Long.toString(boardId);
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        while (result.length()<10)
        {
            result += characters.charAt(random.nextInt(characters.length()));
        }
        this.accessKey = result;

    }

    /**
     * Getter for the password of the board.
     *
     * @return - this board's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for this board's password.
     *
     * @param password - the new password of the board
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Getter for the tags.
     * @return a list of tags for this board
     */
    public List<Tag> getTags(){
        return this.tags;
    }

    /**
     * Setter for the tags.
     * @param tags - the list of tags
     */
    public void setTags(List<Tag> tags){
        this.tags = tags;
    }

    /**
     * Equals method for board items.
     *
     * @param o - Board that is compared with this board
     * @return - true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board = (Board) o;
        return boardId == board.boardId && title.equals(board.title) && lists.equals(board.lists) && accessKey.equals(board.accessKey) && password.equals(board.password);
    }

    /**
     * Hashcode method for the boards.
     *
     * @return - this board's hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(boardId, title, lists, accessKey, password);
    }


    public String getBackgroundColorDefault() {
        return backgroundColorDefault;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getTextColorDefault() {
        return textColorDefault;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getListBackgroundColorDefault() {
        return listBackgroundColorDefault;
    }

    public String getListBackgroundColor() {
        return listBackgroundColor;
    }

    public String getListTextColorDefault() {
        return listTextColorDefault;
    }

    public String getListTextColor() {
        return listTextColor;
    }

    public String getCardFontColor() {
        return cardFontColor;
    }

    public void setCardFontColor(String cardFontColor) {
        this.cardFontColor = cardFontColor;
    }

    public String getCardBackgroundColor() {
        return cardBackgroundColor;
    }

    public void setCardBackgroundColor(String cardBackgroundColor) {
        this.cardBackgroundColor = cardBackgroundColor;
    }

    public List<ColorScheme> getSchemes() {
        return schemes;
    }

    public void setListBackgroundColor(String listBackgroundColor) {
        this.listBackgroundColor = listBackgroundColor;
    }

    public void setListTextColor(String listTextColor) {
        this.listTextColor = listTextColor;
    }
}
