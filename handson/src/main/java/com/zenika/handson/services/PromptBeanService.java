package com.zenika.handson.services;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PromptBeanService {

    private final DefaultListableBeanFactory beanFactory;

    /**
     * Constructor for PromptBeanService.
     *
     * @param beanFactory The DefaultListableBeanFactory to retrieve bean definitions.
     */
    public PromptBeanService(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Retrieves the description of a bean definition by its name.
     *
     * @param beanName the name of the bean
     * @return the description of the bean definition, or a default message if no description is available
     */
    public String getBeanDefinitionDescription(String beanName) {
        var benDefinition = beanFactory.getBeanDefinition(beanName);
        var beanDefinitionDescription = benDefinition.getDescription();
        return beanDefinitionDescription != null ? beanDefinitionDescription : "No description available";
    }

    /**
     * Retrieves the descriptions of all bean definitions in the provided set of bean names.
     *
     * @param beans a set of bean names
     * @return a string containing the descriptions of all bean definitions
     */
    public String getAllBeansDefinitionDescriptions(Set<String> beans) {
        StringBuilder beansDefinitions = new StringBuilder();
        for (var beanName : beans) {
            var beanDefinitionDescription = getBeanDefinitionDescription(beanName);
            beansDefinitions.append(String.format("\n\n%s: %s\n\n", beanName, beanDefinitionDescription));
        }
        return beansDefinitions.toString();
    }
}
