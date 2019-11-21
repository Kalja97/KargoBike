package com.example.kargobike.viewmodel.order;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.Entities.Order;
import com.example.kargobike.repository.OrderRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class OrderListViewModel extends AndroidViewModel {

        private OrderRepository repository;

        private Application application;

        // MediatorLiveData can observe other LiveData objects and react on their emissions.
        private final MediatorLiveData<List<Order>> observableOrders;

        public OrderListViewModel(@NonNull Application application,
                                  OrderRepository orderRepository) {
            super(application);

            this.application = application;
            this.repository = orderRepository;

            observableOrders = new MediatorLiveData<>();
            // set by default null, until we get data from the database.
            observableOrders.setValue(null);
/*
            LiveData<List<Order>> orders = repository.getOrders(application);

            // observe the changes of the client entity from the database and forward them
            observableOrders.addSource(orders, observableOrders::setValue);

 */
        }

        /**
         * A creator is used to inject the account id into the ViewModel
         */
        public static class Factory extends ViewModelProvider.NewInstanceFactory {

            @NonNull
            private final Application application;

            private final OrderRepository repository;

            public Factory(@NonNull Application application) {
                this.application = application;
                repository = ((BaseApp) application).getOrderRepository();
            }

            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                //noinspection unchecked
                return (T) new OrderListViewModel(application, repository);
            }
        }

        /**
         * Expose the LiveData ClientEntity query so the UI can observe it.
         */
        public LiveData<List<Order>> getOrders() {
            return observableOrders;
        }

    public void deleteOrder(Order order, OnAsyncEventListener callback) {
        repository.delete(order, callback, application);
    }
}
