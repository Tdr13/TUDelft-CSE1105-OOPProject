/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.*;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;


import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import commons.Card;
import commons.Listing;
import commons.SubTask;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private static String SERVER = "";
    private static StompSession session;
    private String PORT = "";

    /**
     * This method creates a get request to the server entered by the user.
     *
     * @param userUrl a string representing the url
    // * @param port the port
     * @return a Response object
     */
    public Response checkServer(String userUrl) {
        this.SERVER = "http://" + userUrl;
        //this.PORT = port;

        session = connect("ws://" + userUrl + "/websocket");
        Response response =  ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/connection")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get();


        return response;
    }

    /**
     * This method starts the websockets on a specific port, not working for the moment.
     * @param url - the url of the websocket
     * @return the session
     */
    public StompSession startWebSockets(String url){
        this.session = connect("ws://" + url +"/websocket");
        return session;
    }

    /**
     * Saving the list into database.
     *
     * @param list to be saved into database
     * @return the list
     */
    public Listing saveList(Listing list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), Listing.class);
    }

    /**
     * Sends a post request to the server to update the list.
     *
     * @param list - the updated list
     * @return list
     */
    public Listing editList(Listing list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lists/edit")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), Listing.class);
    }

    /**
     * A method that sends a list to the card, I experimented, it may become redundant later.
     *
     * @param list - the sent list
     * @return Listing
     */
    public Listing sendList(Listing list) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/card/setList")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(list, APPLICATION_JSON), Listing.class);
    }

    /**
     * A method that creates a post request for the card.
     *
     * @param card - the card we are saving
     * @return a subtask
     */
    public Card saveCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/card")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * A method that sends a card to the subtask.
     *
     * @param card - card to send
     * @return the sent card
     */
    public Card sendCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/subtask/setCard")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    /**
     * A method that creates a post request for the subtask.
     *
     * @param subTask - the subtask we are saving
     * @return a subtask
     */
    public SubTask saveSubtask(SubTask subTask) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/subtask")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(subTask, APPLICATION_JSON), SubTask.class);
    }

    /**
     * Fetches all listings from the database.
     *
     * @return all listings stored in a List Object
     */
    public List<Listing> getListings() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Listing>>() {
                });
    }

    /**
     * Method that fetches a list by its id.
     *
     * @param id - list to search for into DB
     * @return the result of the query
     */
    public Listing getListingsById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lists/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Listing>() {
                });
//            .getBy(new GenericType<List<Listing>>() {});
    }

    /**
     * Update a list by changing its name.
     *
     * @param id the id of the list to be updated
     * @param newName the new name
     * @return The updated list
     */

    public Listing updateList(long id, String newName) {
        Listing currentList = getListingsById(id);
        currentList.setTitle(newName);
        return editList(currentList);
    }
    /**
     * Fetches the card with the provided id from the database.
     * @param id The id of the card to search for
     * @return The card with the needed ID
     */
    public Card getCardsById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Card>() {
                });
//            .getBy(new GenericType<List<Listing>>() {});
    }

    /**
     * Updates the card with the new parameters.
     * @param id The ID of the card to be updated
     * @param newName Its new name (more parameters may be added)
     * @return The updated card, if required
     */
    public Card updateCard(long id, String newName) {
        Card currentCard = getCardsById(id);
        currentCard.setName(newName);
        return saveCard(currentCard);
    }

    /**
     * Update a card description.
     *
     * @param id id of the card
     * @param description the new description
     * @return the card
     */
    public Card updateCardDescription(long id, String description) {
        Card currentCard = getCardsById(id);
        currentCard.setDescription(description);
        return saveCard(currentCard);
    }



    /**
     * Sends a delete request.
     *
     * @param id - the id of the card we want to delete
     */
    public void deleteCard(long id) {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card/delete/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete();
    }

    /**
     * Deleting a list from the Database.
     *
     * @param id the id of the list to be deleted
     */
    public void deleteList(Long id) {
        ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lists/" + id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .delete();
    }

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    /**
     * Adds a board to the database.
     * @param board the board to be added
     * @return the board saved
     */
    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Gets all boards from the database.
     * @return a list of all boards
     */
    public List<Board> getBoardsFromDB() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Board>>() {
                });
    }

    /**
     * Assigns a board to a listing.
     * @param board the 'parent' board
     * @return the board saved
     */
    public Board sendBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(SERVER).path("api/lists/setBoard")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    /**
     * Fetches a board by its unique id.
     * @param id the board's id
     * @return the board with the corresponding id
     */
    public Board getBoardByID(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/boards/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Board>() {
                });
    }

    /**
     * Gets the port on which the current application is running.
     * @return said port
     */
    public String getPort()
    {
        String response = ClientBuilder.newClient(new ClientConfig()) //
            .target(SERVER).path("api/connection/getServer") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(String.class);
        return response;
    }
    /**
     * Updates the board with the new parameters.
     * @param id The ID of the board to be updated
     * @param newTitle Its new title (more parameters may be added)
     * @return The updated board, if required
     */
    public Board updateBoard(long id, String newTitle) {
        Board currentBoard = getBoardByID(id);
        currentBoard.setTitle(newTitle);
        currentBoard = addBoard(currentBoard);
        sendBoard(currentBoard);
        for (Listing l : currentBoard.getLists())
            editList(l);
        return currentBoard;
    }
}
