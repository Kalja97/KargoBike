package com.example.kargobike.viewmodel.product;

import android.app.Application;

import com.example.kargobike.BaseApp;
import com.example.kargobike.database.entity.Product;
import com.example.kargobike.database.repository.ProductRepository;
import com.example.kargobike.util.OnAsyncEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ProductViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<Product> observableProduct;
    private ProductRepository repository;

    public ProductViewModel(@NonNull Application application,
                            final String productname, ProductRepository productRepository) {

        super(application);

        repository = productRepository;

        observableProduct = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProduct.setValue(null);

        LiveData<Product> product = repository.getProduct(productname);

        //observe the changes of the client entity from the database and forward them
        observableProduct.addSource(product, observableProduct::setValue);

    }

    //get a product
    public LiveData<Product> getProduct() {
        return observableProduct;
    }

    //create product
    public void createProduct(Product product, OnAsyncEventListener callback) {
        ProductRepository.getInstance().insert(product, callback);
    }

    //update product
    public void updateProduct(Product product, OnAsyncEventListener callback) {
        ProductRepository.getInstance().update(product, callback);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;
        private final String productname;
        private final ProductRepository repository;

        public Factory(@NonNull Application application, String productname) {
            this.application = application;
            this.productname = productname;
            this.repository = ((BaseApp) application).getProductRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ProductViewModel(application, productname, repository);
        }
    }
}