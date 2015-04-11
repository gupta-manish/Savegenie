package in.savegenie.savegenie.responses;


import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Order;
import in.savegenie.savegenie.backgroundclasses.OrderItem;
import in.savegenie.savegenie.backgroundclasses.OrderStore;

public class OrderDetailResponse {
	public ArrayList<OrderItem> orderItemList;
	public Order order;
	public OrderStore store;
	
	public OrderDetailResponse(ArrayList<OrderItem> orderItemList,Order order,OrderStore store) {
		this.orderItemList = orderItemList;
		this.order = order;
		this.store = store;
	}
}
