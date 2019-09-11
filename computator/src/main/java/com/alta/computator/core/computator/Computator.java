package com.alta.computator.core.computator;

/**
 * Provides the interface for making computations.
 */
public interface Computator<Model extends ComputatorEvaluableModel, Args extends ComputatorArgs> {

    /**
     * Initializes the model.
     *
     * @param model - the model to be initialized.
     * @param args  - the arguments to be used for initialization.
     */
    void initialize(Model model, Args args);

    /**
     * Run the computation process.
     *
     * @param model             - the model to be computed.
     * @param args              - the arguments to be used for computation.
     */
    void compute(Model model, Args args);
}
