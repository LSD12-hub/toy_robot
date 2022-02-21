
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static Scanner in = new Scanner(System.in);
    static String [] valid_commands = {"off", "help", "forward", "back", "forward", "right", "left", "sprint", "replay"};
    static boolean is_runnning = true;

    public static int position_x = 0;
    public static int position_y = 0;
    
    public static int direction = 1;

    static String [] non_playables = {"off", "help", "replay"};
    static ArrayList<String> history = new ArrayList<String>();

    static boolean is_silent = false;
    static boolean is_reversed = false;

    public static void main(String[] args) throws Exception {
        robot();
    }

    /**
     * gets name of the user
     * @return the name of the robot
     */
    public static String get_name(){
        System.out.print("What do you want to name your robot? ");
        return in.nextLine();
    }

    
    /**
     * checks to see if num is digit
     * @param num
     * @return boolean value
     */
    public static boolean is_int(String num){
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * print current position of the robot
     * @param robot_name
     * @return string 
     */
    public static String print_position(String robot_name) {
        return " > " + robot_name + " now at position (" + position_x +","+ position_y +").";
    }


    /**
     * splits command and returns 2 items in the array 
     * @param command
     * @return array
     */
    public static String [] split_command(String command) {
        String [] args = command.split(" ", 2);
        if (args.length > 1) {
            return args;
        }else{
            String [] temp_array = new String[2];
            temp_array[0] = args[0];
            return temp_array;
        }
    }


    /**
     * validates the command
     * @param command
     * @return boolean value
     */
    private static boolean validate_command(String command) {
        String [] args = split_command(command);

        if (args[0].toLowerCase().equals("replay")) {
            if(args[1] == (null)) 
                return true;

            ArrayList<String> args1 = new ArrayList<String>();
            for(String s : args[1].split(" ")) args1.add(s);
            
            if (args1.contains("silent")) {
                args1.remove("silent");
            }
            if (args1.contains("reversed")) {
                args1.remove("reversed");
            }

            if (args1.size() > 0){
                if (is_int(args1.get(0))) {
                    return true;
                }else{
                    String [] nums = args1.get(0).split("-");
                    try{
                        return is_int(nums[0]) && is_int(nums[1]) &&
                            Integer.parseInt(nums[0]) > Integer.parseInt(nums[1]);
                    }catch(Exception e){
                        return false;
                    }
                }
            }else{
                return true;
            }
        }

        for(String s : valid_commands){
            if (s.equals(args[0].toLowerCase())){
                return args[1] == null || is_int(args[1]);
            }
        }
        return false;
    }


    /**
     * Verifies the name commands of the robot
     * @param robot_name
     * @return verfied command
     */
    private static String get_command(String robot_name) {
        System.out.print(robot_name + ": What must I do next? ");
        String command = in.nextLine();

        while (command.length() != 0 && !validate_command(command)){
            System.out.println(robot_name + ": Sorry, I did not understand '"+command+"'.");
            System.out.print(robot_name + ": What must I do next? ");
            command = in.nextLine();
        }
        return command.toLowerCase();
    }


    /**
     * Shows the commands and functions
     * @return a string commands
     */
    public static String do_help(){
        return "OFF   - Shut down robot\n"
        +"HELP  - provide information about commands\n";
    }


    /**
     * updates the position of the robot
     * @param steps
     * @return boolean value
     */
    public static boolean update_position(int steps){
        int x = position_x;
        int y = position_y;

        if (direction == 1) {
            position_y += steps;
        }else if(direction == 2){
            position_x += steps;
        }else if(direction == 3){
            position_y -= steps;
        }else if(direction == 4){
            position_x -= steps;
        }
        
        if (Math.abs(position_x) > 100 || Math.abs(position_y) > 200){
            position_x = x;
            position_y = y;
            return false;
        }
        return true;
    }
    

    /**
     * makes the robot move forward
     * @param robot_name
     * @param steps
     * @return string that shows robot moved forward
     */
    public static String do_forward(String robot_name, int steps){
        if(update_position(steps))
            return " > " + robot_name + " moved forward by " + steps + ".";
        return robot_name + ": Sorry, I cannot go outside my safe zone.";
    }


    /**
     * makes the robot move back
     * @param robot_name
     * @param steps
     * @return string that shows robot moved back
     */
    public static String do_back(String robot_name, int steps){
        if(update_position(-steps))
            return " > " + robot_name + " moved back by " + steps + ".";
        return robot_name + ": Sorry, I cannot go outside my safe zone.";
    }


    /**
     * makes the robot move right
     * @param robot_name
     * @return
     */
    public static String do_right(String robot_name){
        direction++;
        if (direction == 5) {
            direction = 0;
        }

        return " > " + robot_name + " turned right.";
    }


    /**
     * makes the robot move right
     * @param robot_name
     * @return
     */
    public static String do_left(String robot_name){
        direction--;
        if (direction == 0) {
            direction = 4;
        }
        
        return " > " + robot_name + " turned left.";
    }


    /**
     * uses recurion to move the robot forward
     * @param robot_name
     * @param steps
     * @return
     */
    public static String do_sprint(String robot_name, int steps){
        String output = do_forward(robot_name, steps);
        if (steps == 1 || output.equals(robot_name + ": Sorry, I cannot go outside my safe zone.")) {
            return output;
        }
        System.out.println(output);
        return do_sprint(robot_name, steps - 1);
    }


    /**
     * filters out the list
     * @return new list
     */
    public static ArrayList<String> filter_history_array(ArrayList<String> tempArrayList){  
        ArrayList<String> temp_list = history;   
        // filter out the list properly
        for(String i : history){
            for(String j : non_playables){
                if (j.contains(i)) {
                    temp_list.remove(i);
                }
            }
        }
        
        if (is_reversed) {
            ArrayList<String> reversed_list = new ArrayList<>();
            for (int i = temp_list.size() - 1; i >= 0; i--) {
                reversed_list.add(history.get(i));
            }
            temp_list = reversed_list;
        }

        if (tempArrayList.size() > 0) {
            ArrayList<String> sliced_list = new ArrayList<>();
            String [] nums = tempArrayList.get(0).split("-");

            if (nums.length > 1) {
                for (int i = Integer.parseInt(nums[1]) - 1; i < Integer.parseInt(nums[0]) - 1; i++) {
                    sliced_list.add(history.get(i));
                }    
            }else{
                for (int i = Integer.parseInt(nums[0]) - 1; i < history.size(); i++) {
                    sliced_list.add(history.get(i));
                }
            }
            temp_list = sliced_list;
        }
        return temp_list;
    }


    /**
     * replays the list of commands
     * @param robot_name
     * @param args
     * @return String
     */
    public static String do_replay(String robot_name, String args) {
        int count = 0;

        ArrayList<String> tempArrayList = new ArrayList<String>();
        if (args == null) args = " ";
        for(String s : args.split(" ")) tempArrayList.add(s);

        if(tempArrayList.contains("silent")){
            is_silent = true;
            tempArrayList.remove("silent");
        }
        if(tempArrayList.contains("reversed")){
            is_reversed = true;
            tempArrayList.remove("reversed");
        }
    
        history = filter_history_array(tempArrayList);

        for(String command : history){
            handle_command(robot_name, command);
            count++;
        }

        if (is_silent && is_reversed) {
            is_reversed = false;
            is_silent = false;
            return " > " + robot_name + " replayed " + count + " commands in reverse silently.";
        }else if (is_reversed) {
            is_reversed = false;
            return " > " + robot_name + " replayed " + count + " commands in reverse.";
        }else if (is_silent) {
            is_silent = false;
            return " > " + robot_name + " replayed " + count + " commands silently.";
        }
        return " > " + robot_name + " replayed " + count + " commands";
    }


    /**
     * handles all the commands
     * @param robot_name
     * @param command
     */
    public static void handle_command(String robot_name, String command){
        String [] args = split_command(command);
        String output = "";

        if(args[0].equals("off")){
            System.out.println(robot_name + ": Shutting down..");
            System.exit(0);
        }else if(args[0].equals("help")){
            output = do_help();
        }else if(args[0].equals("forward")){
            output = do_forward(robot_name, Integer.parseInt(args[1]));
        }else if(args[0].equals("back")){
            output = do_back(robot_name, Integer.parseInt(args[1]));
        }else if(args[0].equals("right")){
            output = do_right(robot_name);
        }else if(args[0].equals("left")){
            output = do_left(robot_name);
        }else if(args[0].equals("sprint")){
            output = do_sprint(robot_name, Integer.parseInt(args[1]));
        }else if(args[0].equals("replay")){
            output = do_replay(robot_name, args[1]);
        }

        if (!is_silent) {
            System.out.println(output);
            System.out.println(print_position(robot_name));
        }
    }
    

    public static void robot(){
        String robot_name = get_name();
        System.out.println(robot_name + ": Hello kiddo!");

        String command = get_command(robot_name);
        while(is_runnning){
            handle_command(robot_name, command);
            history.add(command);
            command = get_command(robot_name);
        }
    }
}
