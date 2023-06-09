package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import java.util.Objects;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cardId;
    private String description;
    private String name;
    private Date dueDate;
    @ManyToMany()
    @JoinTable(
            name = "tagged_cards",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
    @JsonManagedReference
    @OneToMany(
        mappedBy = "card",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )

    private List<SubTask> subTasks;
    private boolean complete;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "list_id")
    private Listing list;

    private String fontColor;
    private String backgroundColor;

    private String schemeName;

    /**
     *
     * Constructor for the card class.
     * @param description - description of the card
     * @param name - Name of the card
     * @param dueDate - The due date of the card
     * @param tags - List with tags assigned to card (can be empty)
     * @param subTasks - List of smaller simple subtasks of this card (can be empty)
     * @param list - the list in which the card is
     * @param fontColor - the font color
     * @param backgroundColor - the background color
     * @param schemeName - the name of the scheme
     */
    public Card(String description, String name, Date dueDate, List<Tag> tags, List<SubTask> subTasks, Listing list, String fontColor, String backgroundColor, String schemeName) {
        this.description = description;
        this.name = name;
        this.dueDate = dueDate;
        this.tags = tags;
        this.subTasks = subTasks;
        this.complete = false;
        this.list = list;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.schemeName = schemeName;
    }

    /**
     * Default constructor.
     */
    @SuppressWarnings("unused")
    public Card() {

    }

    /**
     * Getter for the id.
     * @return the id of the card
     */
    public long getCardId() {
        return cardId;
    }
    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    /**
     * Getter for the description.
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the description.
     * @param description - the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the name.
     * @return the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name.
     * @param name - the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the due date.
     * @return the due date of the card
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Setter for the due date.
     * @param dueDate - the new due date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Getter for the tags.
     * @return the tags of the card
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Alters the tags list with a new one.
     * @param tags - the new tags list
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Getter for the mini tasks.
     * @return the mini tasks of the card
     */
    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    /**
     * Alters the mini tasks list with a new one.
     * @param subTasks - the new array list
     */
    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    /**
     * Getter for the completion.
     * @return whether the task is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Alters the status of the task.
     * @param complete - the new status
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    /**
     * Equals method for the Card class.
     * @param o - the object with which we check for equality
     * @param lists - checks whether the method has been called from a list and doesn't check if the lists are equal as this makes the code go in a loop
     * @return - a boolean based on the outcome
     */
    public boolean equals(Object o, boolean lists) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        if (lists)
            return cardId == card.cardId && complete == card.complete && description.equals(card.description) && name.equals(card.name) && tags.equals(card.tags) && subTasks.equals(card.subTasks) && fontColor.equals(card.fontColor) && backgroundColor.equals((card.backgroundColor)) && schemeName.equals(card.schemeName);
//        return cardId == card.cardId && complete == card.complete && description.equals(card.description) && name.equals(card.name) && tags.equals(card.tags) && subTasks.equals(card.subTasks) && list.equals(card.list) && fontColor.equals(card.fontColor) && backgroundColor.equals((card.backgroundColor))&& schemeName.equals(card.schemeName);
        return this.equals(o);
    }
    /**
     * Equals method for the Card class. (FOR TESTING PURPOSES ONLY)
     * @param o - the object with which we check for equality
     * @return - a boolean based on the outcome
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return cardId == card.cardId && complete == card.complete && Objects.equals(description, card.description) && Objects.equals(name, card.name) && Objects.equals(dueDate, card.dueDate) && Objects.equals(tags, card.tags) && Objects.equals(subTasks, card.subTasks) && Objects.equals(fontColor, card.fontColor) && Objects.equals(backgroundColor, card.backgroundColor) && Objects.equals(schemeName, card.schemeName);
    }

    /**
     * Getter for the list of the card.
     * @return the list of this card
     */
    public Listing getList() {
        return list;
    }

    /**
     * Setter for the list.
     * @param list - the list we are setting
     */
    public void setList(Listing list) {
        this.list = list;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardId=" + cardId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", dueDate=" + dueDate +
                ", tags=" + tags +
                ", subTasks=" + subTasks +
                ", complete=" + complete +
                ", list=" + list +
                ", fontColor='" + fontColor + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", schemeName='" + schemeName + '\'' +
                '}';
    }

    /**
     * A hashcode value of the card.
     * @return - the new generated hash code of the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardId, description, name, dueDate, tags, subTasks, complete);
    }
}
