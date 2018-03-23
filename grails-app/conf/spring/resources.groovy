import org.kefirsf.bb.BBProcessorFactory

// Place your Spring DSL code here
beans = {
    textProcessorFactory(BBProcessorFactory) { bean ->
        bean.factoryMethod = 'getInstance'
    }

    textProcessor(textProcessorFactory: 'create')
}
