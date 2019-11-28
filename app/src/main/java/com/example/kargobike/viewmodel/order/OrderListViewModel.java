package com.example.kargobike.viewmodel.order;

import android.app.Application;

import com.example.kargobike.database.entity.OrderF;
import com.example.kargobike.database.repository.OrderRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static com.example.kargobike.database.repository.OrderRepository.*;

public class OrderListViewModel extends AndroidViewModel {

    private OrderRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<OrderF>> observableOrders;

    public OrderListViewModel(@NonNull Application application, OrderRepository orderRepository) {
        super(application);

        repository = orderRepository;

        observableOrders = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableOrders.setValue(null);

        LiveData<List<OrderF>> locations = repository.getAllOrders();

        // observe the changes of the entities from the database and forward them
        observableOrders.addSource(locations, observableOrders::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final OrderRepository orderRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            orderRepository = getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new OrderListViewModel(application, orderRepository);
        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<OrderF>> getOrders() {
        return observableOrders;
    }

    public void deleteOrder(OrderF order) {
        repository.delete(order, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {}

            @Override
            public void onFailure(Exception e) {}
        });
    }
}
