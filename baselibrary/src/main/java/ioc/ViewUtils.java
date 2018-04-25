package ioc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by pc on 2018/4/25.
 */

public class ViewUtils {


    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);

    }


    public static void inject(View source, Object object) {
        inject(new ViewFinder(source),object);
    }

    public static void inject(View source){
        inject(new ViewFinder(source),null);
    }


    private static void inject(ViewFinder viewFinder,Object object){
       injectFields(viewFinder,object);
       injectEvent(viewFinder,object);
    }

    /**
     * inject the fields such as id bind the view
     * @param viewFinder
     * @param object
     */
    private static void  injectFields(ViewFinder viewFinder ,Object object){

        //get the class
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        //search the annotion if has viewbyid
        for (Field field : fields) {
            ViewById viewById = field.getAnnotation(ViewById.class);
            if (viewById != null){

                //bind the view
                View view = viewFinder.findViewById(viewById.value());

                if (view != null){
                    //reflect inject to realize the bind
                    field.setAccessible(true);

                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        Log.d("TAG", "injectFields: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void injectEvent(ViewFinder viewFinder,Object object){
        //get the class
        Class<?> clazz  = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] values = onClick.values();
                for (int i = 0; i < values.length; i++) {
                    View view = viewFinder.findViewById(values[i]);
                    if (view != null) {
                        method.setAccessible(true);
                        view.setOnClickListener(new DeclaredOnClickListener(object, method));
                    }
                }

            }
        }
    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private final Object object;
        private final Method method;

        private Method mResolvedMethod;
        private Context mResolvedContext;

        public DeclaredOnClickListener(@NonNull Object object, @NonNull Method method) {
            this.object = object;
            this.method = method;
        }

        @Override
        public void onClick(@NonNull View v) {
            if (method != null) {
                try {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (Class<?> clas : parameterTypes) {
                        String parameterName = clas.getName();
                        Log.d("TAG", "参数名称:" + parameterName );
                    }
                    if (parameterTypes.length == 0){
                        method.invoke(object);
                    }
                    else {
                        method.invoke(object,v);
                    }

                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Could not execute non-public method for android:onClick", e);
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException(
                            "Could not execute method for android:onClick", e);
                }
            }


        }

    }
}
