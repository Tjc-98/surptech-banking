package org.surptech.creditprofile.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Provider for accessing Spring ApplicationContext from non-Spring managed beans.
 *
 * This component enables Spring bean access from classes that are instantiated manually
 * using the 'new' keyword (such as procedure classes) instead of being managed by Spring.
 *
 * Usage: ApplicationContextProvider.getBean(BeanClass.class)
 *
 * Note: While this pattern provides flexibility, consider using Spring-managed singletons
 * where possible, as they provide better testability and lifecycle management.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.context = applicationContext;
    }

    /**
     * Retrieves a Spring bean by type from the application context.
     *
     * @param <T>       the type of the bean
     * @param beanClass the class of the bean to retrieve
     * @return the bean instance
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException if the bean cannot be found
     * @throws BeansException if an error occurs while retrieving the bean
     * @throws IllegalStateException if the application context has not been initialized
     */
    public static <T> T getBean(Class<T> beanClass) {
        if (context == null) {
            throw new IllegalStateException(
                "Application context has not been initialized. " +
                "ApplicationContextProvider must be instantiated by Spring before use."
            );
        }
        return context.getBean(beanClass);
    }
}
