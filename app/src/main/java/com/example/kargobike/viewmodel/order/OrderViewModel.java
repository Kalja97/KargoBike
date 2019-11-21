package com.example.kargobike.viewmodel.order;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.Entities.Order;
import com.example.kargobike.repository.OrderRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository repository;
    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<Order> observableOrder;

    public OrderViewModel(@NonNull Application application,
                          final String ordername, OrderRepository orderRepository) {

        super(application);

        this.application = application;

        repository = orderRepository;

        observableOrder = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableOrder.setValue(null);

        /*
        LiveData<Order> order = repository.getOrder(ordername, application);


        //observe the changes of the client entity from the database and forward them
        observableOrder.addSource(order, observableOrder::setValue);
  */
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String ordername;

        private final OrderRepository repository;

        public Factory(@NonNull Application application, String ordername, OrderRepository repository) {
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

    /**
     * Expose the LiveData ClientEntity query so the UI can observe it.
     */
    public LiveData<Order> getOrder() {
        return observableOrder;
    }

    public void createOrder(Order order, OnAsyncEventListener callback) {
        repository.insert(order, callback, application);
    }

    public void updateOrder(Order order, OnAsyncEventListener callback) {
        repository.update(order, callback, application);
    }

    public void deleteOrder(Order order, OnAsyncEventListener callback) {
        repository.delete(order, callback, application);
    }
}