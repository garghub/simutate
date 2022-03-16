# simutate

stuff you require before runnning

1. defects4j (available here: https://github.com/rjust/defects4j) for simulation task, i.e., for compiling and running tests
2. srcML (available here: https://www.srcml.org/)
3. src2abs (available here: https://github.com/micheletufano/src2abs) for abstraction task (if you wish to perform it)

-----------------------------------------------------------------------------------------------------------------------------

mvn clean package

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar [arguments]


options based on tasks:

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar simulate codebert Math

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar simulate codebert Math Math_1 <-- this will run simulation for only Math_1 bug and skip the rest

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar flatten codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar flatten codebert Math <-- this will run flattening for only Math related bugs and skip the rest

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar flatten codebert Math Math_1 <-- this will run flattening for only Math_1 bug id and skip the rest

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar flattenfixes codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar flattenfixes codebert Math <-- this will run flattening for only Math related fixes and skip the rest

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar flatten codebert Math Math_1 <-- this will run flattening for only Math_1 fix id and skip the rest

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar getalltests codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar processsourcepatches codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar abstract

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar unabstract

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar compare codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar comparefixes codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar processlocationmapping codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar getfailingtests codebert

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar processsubsumingmutantdiff ibir

java -jar /home/agarg/ag/simutate/target/simutate-1.0.jar consolidatechangedlinesdatabymutants [codebert/ibir]

-----------------------------------------------------------------------------------------------------------------------------

options based on tasks provided by python scripts (here please change /home/agarg/ag/simutate to point to your repository location):

python3 /home/agarg/ag/simutate/scripts/measuring_syntactic_similarity.py fixes codebert Cli <-- if bugs are to be processed then replace "fixes" with "bugs"

python3 /home/agarg/ag/simutate/scripts/measuring_syntactic_similarity.py fixes codebert Cli Cli_1 <-- if bugs are to be processed then replace "fixes" with "bugs"

python3 /home/agarg/ag/simutate/scripts/measuring_overall_syntactic_similarity.py fixes codebert <-- if bugs are to be processed then replace "fixes" with "bugs"

python3 /home/agarg/ag/simutate/scripts/combine_semantic_and_syntactic.py fixes codebert <-- if bugs are to be processed then replace "fixes" with "bugs"

python3 /home/agarg/ag/simutate/scripts/plot.py codebert

-----------------------------------------------------------------------------------------------------------------------------

NOTE: please do not forget to modify below variables in data.java file to specify your desired repository locations and dependencies

static String dirMain = isWindows ? "D:/ag/github/mutants_sensitivity" : "/home/agarg/ag/mutation";

static String strInitialCommandForDefects4j01 = "/home/agarg/ag/defects4j/defects4j/framework/bin/defects4j";

static String strInitialCommandForsrc2abs02 = " && java -jar C:/GitHub/src2abs/src2abs-master/target/src2abs-0.1-jar-with-dependencies.jar single method ";

-----------------------------------------------------------------------------------------------------------------------------

below is an example:

java -jar D:\ag\github\simutate\target\simutate-1.0.jar

please pass below as arguments and try again

1. a task to perform (e.g. abstract / unabstract / processsourcepatches / simulate / flatten /  / flattenfixes / getalltests / compare / comparefixes / getfailingtests / processlocationmapping / processsubsumingmutantdiff / consolidatechangedlinesdatabymutants )

NOTE: for task "simulate", please pass below as additional arguments and try again

Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)

Additional 2. project name to perform simulation for (e.g. Cli)

and

Also for tasks "flatten", "flattenfixes", "processsourcepatches", "getalltests", "compare", "comparefixes", "getfailingtests", "processlocationmapping", "processsubsumingmutantdiff", and "consolidatechangedlinesdatabymutants", please pass below as additional arguments and try again

Additional 1. mutant directory technique suffix (e.g. nmt / codebert / ...)


Optional parameters:

Optional parameters for task "simulate" -

Optional 1. bug id to perform simulation for (e.g. Cli_1 / Cli_2 / ...)

Optional parameters for task "flatten" and "flattenfixes" -

Optional 1. project name to perform flattening for (e.g. Cli)

Optional 2. bug id to perform flattening for (e.g. Cli_1 / Cli_2 / ...)

-----------------------------------------------------------------------------------------------------------------------------

please feel free to fork it, modify and use it as per your convenience.

cheers!
