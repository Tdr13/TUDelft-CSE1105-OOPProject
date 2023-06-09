package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private Card card;
    private List<Tag> tags = new ArrayList<>();
    private List<SubTask> subtasks = new ArrayList<>();
    Date dueDate;

    private Listing listing;

    @BeforeEach
    void setUp() {
        Tag tag1 = new Tag("tag1");
        this.tags.add(tag1);
        Tag tag2 = new Tag("tag2");
        this.tags.add(tag2);

        SubTask s1 = new SubTask("s1",card);
        this.subtasks.add(s1);

        SubTask s2 = new SubTask("s2",card);
        this.subtasks.add(s2);

        dueDate = new Date();

        listing = new Listing("list",null);

        this.card = new Card("desc","name", dueDate, tags, subtasks, listing,"","","");
    }

    @Test
    void testDefaultConstructor() {
        card = new Card();
        assertNotNull(card);
    }

    @Test
    void getCardId() { // the id is autogenerated
    }

    @Test
    void setCardId() {
        card.setCardId(413);
        assertEquals(413, card.getCardId());
    }

    @Test
    void getDescription() {
        assertEquals("desc", card.getDescription());
    }

    @Test
    void setDescription() {
        card.setDescription("desc2");
        assertEquals("desc2",card.getDescription());
    }

    @Test
    void getName() {
        assertEquals("name",card.getName());
    }

    @Test
    void setName() {
        card.setName("name2");
        assertEquals("name2",card.getName());
    }

    @Test
    void getDueDate() {
        assertNotNull(card.getDueDate());
    }

    @Test
    void setDueDate() {
        Date dueDate2 = new Date();
        card.setDueDate(dueDate2);
        assertEquals(dueDate2, card.getDueDate());
    }

    @Test
    void getTags() {
        assertEquals(tags,card.getTags());
    }

    @Test
    void setTags() {
        List<Tag> newTags = new ArrayList<>();

        Tag tag1 = new Tag("tag1");
        newTags.add(tag1);

        Tag tag2 = new Tag("tag2");
        newTags.add(tag2);

        Tag tag3 = new Tag("tag3");
        newTags.add(tag3);

        card.setTags(newTags);
        assertEquals(newTags,card.getTags());
    }

    @Test
    void getSubTasks() {
        assertEquals(subtasks, card.getSubTasks());
    }

    @Test
    void setSubTasks() {
        List<SubTask> newSubtasks = new ArrayList<>();

        SubTask s1 = new SubTask("s1",card);
        newSubtasks.add(s1);

        SubTask s2 = new SubTask("s2",card);
        newSubtasks.add(s2);

        card.setSubTasks(newSubtasks);
        assertEquals(newSubtasks, card.getSubTasks());
    }

    @Test
    void isComplete() {
        assertFalse(card.isComplete());
    }

    @Test
    void setComplete() {
        card.setComplete(true);
        assertTrue(card.isComplete());
    }

    @Test
    void getSchemeName() {
        assertEquals("", card.getSchemeName());
    }

    @Test
    void setSchemeName() {
        card.setSchemeName("Summer");
        assertEquals("Summer", card.getSchemeName());
    }

    @Test
    void getList(){
        assertEquals(listing,card.getList());
    }

    @Test
    void setList() {
        Listing list2 = new Listing("list",null);
        card.setList(list2);
        assertEquals(list2, card.getList());
    }
    @Test
    void getBackgroundColor() {
        assertEquals("", card.getBackgroundColor());
    }

    @Test
    void setBackgroundColor() {
        card.setBackgroundColor("yellow");
        assertEquals("yellow", card.getBackgroundColor());
    }

    @Test
    void getFontColor() {
        assertEquals("", card.getFontColor());
    }

    @Test
    void setFontColor() {
        card.setFontColor("green");
        assertEquals("green", card.getFontColor());
    }

    @Test
    void testEquals() {
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        assertEquals(card1, card2);
    }

    @Test
    void testNotEquals(){
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc2", "card", new Date(), tags, subtasks, listing,"","","");
        assertNotEquals(card1, card2);
    }

    @Test
    void testEqualsNotInListing() {
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        assertTrue(card1.equals(card2, false));
    }

    @Test
    void testNotEqualsNotInListing(){
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc2", "card", new Date(), tags, subtasks, listing,"","","");
        assertFalse(card1.equals(card2, false));
    }

    @Test
    void testEqualsInListing() {
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc", "card", new Date(), tags, subtasks, null,"","","");
        assertTrue(card1.equals(card2, true));
    }

    @Test
    void testNotEqualsInListing(){
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc2", "card", new Date(), tags, subtasks, null,"","","");
        assertFalse(card1.equals(card2, true));
    }

    @Test
    void testHashCode() {
        Card card1 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        Card card2 = new Card("desc", "card", new Date(), tags, subtasks, listing,"","","");
        assertEquals(card1.hashCode(),card2.hashCode());
    }

    @Test
    void testToString() {
        String toString = "Card{" +
                "cardId=" + card.getCardId() +
                ", description='" + "desc" + '\'' +
                ", name='" + "name" + '\'' +
                ", dueDate=" + dueDate +
                ", tags=" + tags +
                ", subTasks=" + subtasks +
                ", complete=" + false +
                ", list=" + listing +
                ", fontColor='" + '\'' +
                ", backgroundColor='" + '\'' +
                ", schemeName='" + '\'' +
                '}';
        assertEquals(toString, card.toString());
    }
}