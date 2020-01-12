package com.example.kargobike.viewmodel.product;

import android.app.Application;

import com.example.kargobike.database.entity.Product;
import com.example.kargobike.database.repository.ProductRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Product>> observableProducts;
    private ProductRepository repository;

    public ProductListViewModel(@NonNull Application application, ProductRepository productRepository) {
        super(application);

        repository = productRepository;

        observableProducts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProducts.setValue(null);

        LiveData<List<Product>> products = repository.getAllProducts();

        // observe the changes of the entities from the database and forward them
        observableProducts.addSource(products, observableProducts::setValue);
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<Product>> getProducts() {
        return observableProducts;
    }

    public void deleteProduct(Product product) {
        repository.delete(product, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final ProductRepository productRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            productRepository = ProductRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductListViewModel(application, productRepository);
        }
    }
}
