# k_nearest_neighbor_algorithm

Summary:

Coursework for Fundamentals of Java at Lewis University.

Compares a set of test data to a set of training data, predicts a label based on the k-Nearest Neighbor algorithm, then measures the accuracy of made predictions.  Program requires 2 CSV files of data to execute as desired.  

For example, if a test flower has petal 1 by 4 cm and sepal 2 by 2 cm, this program will check which flower in the training data is the closest match, predict the flower id, then compare this prediction to a provided correct result to self-score the accuracy of predictions.
 
Program Notes:

    1. Accepts a .csv file of any length, however, only the first 75 lines are read.  
    To change total number of samples, change 'final int NUM_SAMPLES' as desired.

    2. Each test/training file should have 4 type double attributes and 1 String identifier.

    3. Imperfect data [missing fields, wrong data type] may affect accuracy, however, 
    the program should still run successfully.

    4. The main method manually closes Scanner resource 'System.in' before exit. 
    No 'try-with-resources' is used since this would block successful recursion
    from the catch block after an exception, since 'System.in' can only run once.
    If this class is used in a larger program, just keep the Scanner stream open.

    5. Program is kept to a single java class as per the coursework specification.
