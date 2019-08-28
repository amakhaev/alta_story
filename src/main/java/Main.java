import com.alta.mediator.Mediator;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.*;
import starter.Starter;
import starter.StarterResolver;

public class Main {

    private static final String START_TYPE_ARG = "startType";

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppInjector());

        CommandLine cmd = parseCommandLineArguments(args);
        Starter starter;
        if (cmd == null) {
            starter = StarterResolver.resolve(null);
        } else {
            starter = StarterResolver.resolve(cmd.getOptionValue(START_TYPE_ARG));
        }

        starter.start(injector);

        /*Injector injector = Guice.createInjector(new AppInjector());
        Mediator mediator = injector.getInstance(Mediator.class);
        mediator.loadSavedGameAndStart();*/
    }

    private static CommandLine parseCommandLineArguments(String[] args) {
        Options options = new Options();
        Option startType = new Option(null, START_TYPE_ARG, true, "The type of start application (game, check).");
        startType.setRequired(false);
        options.addOption(startType);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            return null;
        }
    }
}
