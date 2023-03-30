package server.api;

import commons.Card;
import commons.Listing;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;
//import server.database.ListingRepository;


@RestController
@RequestMapping("api/card")
public class CardSavingController {

    private final CardRepository repo;
    private final SimpMessageSendingOperations msgs;

    private Listing list;



    public CardSavingController(CardRepository repo, SimpMessageSendingOperations msgs) {
        this.repo = repo;
        this.msgs = msgs;
    }

    /**
     * A post method that saves the card into the DB.
     *
     * @param card - the card that we are saving
     * @return card
     */
    @PostMapping(path = {"", "/"})
    public ResponseEntity<Card> add(@RequestBody Card card) {
        if(card == null) return ResponseEntity.badRequest().build();

        card.setList(list);

        msgs.convertAndSend("/topic/card", card);
        Card save = repo.save(card);
        return ResponseEntity.ok(save);
    }

    @PostMapping(path = {"/setList"})
    public ResponseEntity<Listing> getList(@RequestBody Listing list) {

        this.list = list;
        return ResponseEntity.ok(list);
    }

    @DeleteMapping(path = {"delete/{id}"})
    public ResponseEntity<Listing> delete(@PathVariable long id) {
        Card card = repo.findById(id).orElse(null);
        if (card == null) {
            return ResponseEntity.notFound().build();
        }
        msgs.convertAndSend("/topic/card", card);
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * A get method that searches for a card via its ID.
     *
     * @param id the card's ID
     * @return The response entity, containing the desired card
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }
}
