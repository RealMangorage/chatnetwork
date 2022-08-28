    package org.mangorage.chat;
    import org.apache.commons.cli.*;
    import org.mangorage.chat.events.EventHandler;
    import org.mangorage.chat.gui.ClientScreen;
    import org.mangorage.chat.gui.ServerScreen;
    import org.mangorage.chat.sides.*;
    import org.mangorage.chat.packetutils.CommunicationHandler;

    import javax.swing.*;
    import java.io.IOException;

    public class Main {
        public static final CommunicationHandler handler = new CommunicationHandler();
        private static final EventHandler eventHandler = new EventHandler();
        private static int port = 25565;
        private static Server server;
        private static AbstractClient client;

        public static CommunicationHandler getHandler() {
            return handler;
        }
        public static EventHandler getEventHandler() {return eventHandler; }

        public static Server getServer() {return server; }
        public static AbstractClient getClient() {
            return client;
        }
        public static int getPort() {return port; }

        public static void setPort(int Port) {
            port = Port;
        }

        public static void StartServer(int Port) {
            port = Port;
            server = new Server(Port);
        }

        public static void StartClient(String Host, int Port, String Username) {
            port = Port;
            client = new LocalClient(Host, Port, Username);
            client.init();
        }


        public static void initWindow(Side side) {
            JFrame window = new JFrame("Can't Stop, Won't Stop, GameStop");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            if (side == Side.CLIENT) {
                ClientScreen screen = new ClientScreen();
                window.add(screen);
                window.addKeyListener(screen);
            } else if (side == Side.SERVER) {
                ServerScreen screen = new ServerScreen();
                window.add(screen);
                window.addKeyListener(screen);
            }

            window.setResizable(true);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        }


        public static void sleep(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        public static void main(String[] args) {
            Options options = new Options();

            Option isServer = new Option("s", "server", false, "To create a server");
            options.addOption(isServer);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                System.out.println(e.getMessage());
                formatter.printHelp("utility-name", options);

                System.exit(1);
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    if (getClient() != null) {
                        getClient().disconnect();
                    }
                }
            });


            if (cmd.hasOption("server")) {
                initWindow(Side.SERVER);
            } else {
                initWindow(Side.CLIENT);
            }
        }
    }