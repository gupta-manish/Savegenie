package in.savegenie.savegenie.internetcommunication;


public class InternetURL
{
    private static int development = 1;
    public static String dev = "http://dev.savegenie.in/abc/";
    public static String savegenie = "http://dev.savegenie.in/";
    public static String siteURL = ((development == 1)?dev:savegenie);
    public static String ANDROID_LOGIN = dev  + "usermgmt/users/androidLogin.xml";
    public static String USER_SERVED_STORES = dev + "usermgmt/users/userServedStores.xml";
    public static String ANDROID_GET_PRODUCTS = dev + "Pages/androidGetProducts.xml";
    public static String ALL_PRODUCT_IMAGE = dev + "img/allproduct_images/";
    public static String ALL_STORE_IMAGE = dev + "img/store/";
    public static String USER_SERVED_LISTS = dev + "SgenieCarts/androidUserExistList.xml";
    public static String SEND_SELECTED_STORE = dev + "Pages/userChooseStores.xml";
    public static String GET_SELECTED_LIST_ITEMS = dev + "SgenieCarts/androidUserChooseList.xml";
    public static String CREATE_NEW_LIST = dev + "SgenieCarts/androidUserCreateList.xml";
    public static String ADD_REMOVE_FROM_LIST = dev + "SgenieCarts/androidAddToList.xml";
    public static String GET_COMPARE_STORE_ITEMS = dev + "SgenieCarts/compareStore.xml";
    public static String GET_REVIEW_ITEMS = dev + "SgenieCarts/compareStore2.xml";
    public static String GET_SWAP_RESPONSE = dev + "SgenieCarts/androidSwapAvailable.xml";
    public static String GET_MORE_OPTIONS = dev + "SgenieCarts/moreOption.xml";
    public static String GET_ORDER_DETAILS = dev + "Orders/androidOrderDetails.xml";
    public static String GET_DATE_DETAILS = dev + "Orders/androidGetStoreSlotDetails.xml";
    public static String GET_STORE_SLOT_DETAILS = dev + "StoreSlots/androidGetStoreSlots.xml";
    public static String GET_SHIPPING_ADDRESS_LIST = dev + "Orders/androidShippingAddress.xml";
    public static String ENTER_NEW_SHIPPING_ADDRESS = dev + "Orders/androidEnterShippingAddress.xml";
    public static String SAVE_NEW_SHIPPING_ADDRESS = dev + "Orders/androidSaveShippingAddress.xml";
    public static String APPLY_COUPON_CODE = dev + "Orders/androidApplySgenieCoupon.xml";
    public static String CONFIRM_ORDER = dev + "Orders/androidConfirmOrder.xml";
    public static String COMPLETE_ORDER = dev + "Orders/androidCompleteOrder.xml";
    public static String CHECK_LIST = dev + "SgenieCarts/checkList.xml";
    public static String SEND_INACTIVE_LIST_NAME = dev + "SgenieCarts/androidSaveList.xml";
    public static String GET_ALL_PRODUCT_LIST = dev + "Pages/getAndroidFeaturedProducts.xml";
    public static String GET_FILTERED_PRODUCT_LIST = dev + "Pages/getAndroidFeaturedProductsByFilter.xml";
    public static String GET_STORE_DEALS = dev + "Pages/androidGetDeals.xml";
    public static String GET_BASKET_COUNT = dev + "SgenieCarts/androidCountListItems.xml";
    public static String DELETE_ITEM_FROM_LIST = dev + "SgenieCarts/andDeleteProductFromCart.xml";
    public static String GET_MERGED_LIST = dev + "SgenieCarts/andMergeCartItems.xml";
    public static String SELECT_ALL_STORES = dev + "Pages/androidAllStoresByDefault.xml";
    public static String GET_FAV_PRODUCTS = dev + "Pages/androidGetFavProducts.xml";
    public static String GET_FILT_FAV_PRODUCTS = dev + "Pages/androidGetFavProductsByFilter.xml";
    public static String GET_DEAL_DETAIL = dev + "Pages/androidGetDealDetail.xml";
    public static String GET_SEARCH_RESULTS = dev + "Pages/androidSearch.xml";
    public static String GET_FILT_SEARCH_RESULTS = dev + "Pages/androidSearchByFilter.xml";
    public static String SEND_FEEDBACK = dev + "";
    public static String GET_STORE_DETAILS = dev + "Stores/androidStoreDetails.xml";
    public static String GET_MY_ORDER_LIST = dev + "Pages/androidMyOrder.xml";
    public static String UPDATE_COUNT = dev + "Stores/androidUpdateCount.xml";
    public static String GET_USER_PROFILE = dev + "";
    public static String AUTO_COMPLETE_SOCIETY_NAME = dev + "Orders/androidSocietyNames.xml";
    public static String ANDROID_MODIFY_PROFILE = dev + "Users/androidModifyProfile.xml";
    public static String GET_ALL_AREAS = dev + "Areas/androidAllServedAreas.xml";
}