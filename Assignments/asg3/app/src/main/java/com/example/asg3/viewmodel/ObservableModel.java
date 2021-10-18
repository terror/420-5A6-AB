package com.example.asg3.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.LinkedList;
import java.util.List;

/**
 * An abstract super-class for "observable" models, meaning models that can inform "observers" when updates are made
 * to the data they contain.
 * <p>
 * The observers can be informed of changes in a lifecycle appropriate way: if the component is not currently active,
 * it will only be notified once the they are resumed.
 *
 * @param <T>
 */
public abstract class ObservableModel<T> {

  /**
   * Define the stucture of "update" event listeners.
   *
   * @param <T>
   */
  public interface OnUpdateListener<T> {
    void onUpdate(T item);
  }

  // container class for listeners, connecting listeners to their lifecycle (if provided).
  private static class LifecycleListener<T> {
    public Lifecycle lifecycle;
    public OnUpdateListener<T> onUpdateListener;

    public LifecycleListener(Lifecycle lifecycle, OnUpdateListener<T> onUpdateListener) {
      this.lifecycle = lifecycle;
      this.onUpdateListener = onUpdateListener;
    }
  }

  private List<LifecycleListener<T>> lifecycleListeners;

  /**
   * Create an new observable model.
   */
  public ObservableModel() {
    lifecycleListeners = new LinkedList<>();
  }

  /**
   * Add an on-update handler to the observable model.
   *
   * @param lifecycle The lifecycle used to ensure the event runs when the component is resumed.
   * @param listener  The listener.
   */
  public void addOnUpdateListener(Lifecycle lifecycle, OnUpdateListener<T> listener) {
    lifecycleListeners.add(new LifecycleListener<>(lifecycle, listener));
  }

  /**
   * Add an on-update handler to the observable model.
   *
   * @param fragment The fragment to ensure that the event runs when the fragment is resumed.
   * @param listener The listener.
   */
  public void addOnUpdateListener(Fragment fragment, OnUpdateListener<T> listener) {
    addOnUpdateListener(fragment.getLifecycle(), listener);
  }

  /**
   * Add an on-update handler to the observable model without connection to a lifecycle
   *
   * @param listener The listener.
   */
  public void addOnUpdateListener(OnUpdateListener<T> listener) {
    addOnUpdateListener((Lifecycle) null, listener);
  }

  /**
   * Return the model instance, provided to the `onUpdate(..)` handler.
   *
   * @return The model.
   */
  protected abstract T get();

  /**
   * Notify all observers of a data change in the model. Note that all lifecycle dependant components will be notified when they are resumed.
   */
  public void notifyChange() {

    // cycle through all the event listeners defined
    for (LifecycleListener<T> lifecycleListener : lifecycleListeners) {
      // non-lifecycle events are run immediately.
      if (lifecycleListener.lifecycle == null)
        lifecycleListener.onUpdateListener.onUpdate(get());
      // all non-resumed components will have to wait until they are resumes
      else if (lifecycleListener.lifecycle.getCurrentState() != Lifecycle.State.RESUMED) {
        // Create our observer and add it to the lifecycle observer
        final LifecycleEventObserver observer = new LifecycleEventObserver() {
          @Override
          public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event.equals(Lifecycle.Event.ON_RESUME))
              lifecycleListener.onUpdateListener.onUpdate(get());
          }
        };

        lifecycleListener.lifecycle.addObserver(observer);

        // we must call removeObserver() manually when the view lifecycle is destroyed
        lifecycleListener.lifecycle.addObserver(new LifecycleEventObserver() {
          @Override
          public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if (event.equals(Lifecycle.Event.ON_DESTROY))
              lifecycleListener.lifecycle.removeObserver(observer);
          }
        });
      }
      // otherwise the component is resumed and we can continue
      else
        lifecycleListener.onUpdateListener.onUpdate(get());
    }
  }
}
