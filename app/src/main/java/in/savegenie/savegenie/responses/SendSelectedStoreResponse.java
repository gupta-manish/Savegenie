package in.savegenie.savegenie.responses;

public class SendSelectedStoreResponse {
    public String result;
    public String listId;
    public String listName;

    public SendSelectedStoreResponse(String result,String listId,String listName) {
        this.result = result;
        this.listId = listId;
        this.listName = listName;
    }
}
