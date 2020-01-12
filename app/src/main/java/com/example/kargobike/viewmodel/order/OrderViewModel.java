package com.example.kargobike.viewmodel.order;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.database.entity.Order;
import com.example.kargobike.database.repository.OrderRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<Order> observableOrder;
    private OrderRepository repository;

    public OrderViewModel(@NonNull Application application,
                          final String ordername, OrderRepository orderRepository) {

        super(application);

        repository = orderRepository;

        observableOrder = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableOrder.setValue(null);

        LiveData<Order> order = repository.getOrder(ordername);

        //observe the changes of the client entity from the database and forward them
        observableOrder.addSource(order, observableOrder::setValue);

    }

    //get a order
    public LiveData<Order> getOrder() {
        return observableOrder;
    }

    //create order
    public void createOrder(Order order, OnAsyncEventListener callback) {
        OrderRepository.getInstance().insert(order, callback);
    }

    //update order
    public void updateOrder(Order order, OnAsyncEventListener callback) {
        OrderRepository.getInstance().update(order, callback);
    }

    //delete order
    public void deleteOrder(Order order, OnAsyncEventListener callback) {
        OrderRepository.getInstance().delete(order, callback);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String ordername;
        private final OrderRepository repository;

        public Factory(@NonNull Application application, String ordername) {
            this.application = application;
            this.ordername = ordername;
            this.repository = ((BaseApp) application).getOrderRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new OrderViewModel(application, ordername, repository);
        }
    }
}