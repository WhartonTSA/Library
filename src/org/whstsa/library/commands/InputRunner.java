package org.whstsa.library.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputRunner implements Runnable {

    private List<InputListener> inputListenerList = new ArrayList<>();

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            this.fireInputReceived(scanner.nextLine());
        }
    }

    public void addListener(InputListener listener) {
        this.inputListenerList.add(listener);
    }

    public void fireInputReceived(String input) {
        this.inputListenerList.forEach(inputListener -> inputListener.inputReceived(input));
    }

}
