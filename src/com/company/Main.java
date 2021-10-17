package com.company;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private static int N, M;
    private static FileWorker fileWorker = new FileWorker();


    public static final String MAIN_FILE_NAME = "ser";
    public static final String FILES_EXTENSION = ".file";

    public static void main(String[] args)
    {
        //do last save backup
        fileWorker.doFileBackup(MAIN_FILE_NAME);
        System.out.println();

        //search for existing files8
        System.out.println("Available files to load:");
        File[] saves = FileWorker.getSavesList(".", FILES_EXTENSION);
        int counter = 0;
        for (File f : saves)
        {
            ++counter;
            System.out.println(counter + ". " + f.getName());
        }

        boolean flag = counter > 0;
        if(!flag)
        {
            flag = true;
            System.out.println("No suitable files found\n");
        }
        else
        {
            System.out.println("\nEnter a number of file to load. Enter '0' to skip.");
            int f = readInteger(0, counter);
            if (f != 0)
            {

                fileWorker.deserializeFastJSON(saves[f - 1].getName());
                flag = false;
            }
            else
            {
                System.out.println("File loading skipped.");
                flag = true;
            }
        }

        if(flag)
        {
            //handle user input
            System.out.println("Enter M value");
            M = readInteger();
            System.out.println("Enter N value");
            N = readInteger();

            //fill storage with figures
            System.out.println("Filling lists with figures...");
            fileWorker.fillListRandomly(Circle.class, M);
            fileWorker.fillListRandomly(Cone.class, N);
        }

        //print figures info
        System.out.println("Circle list:");
        fileWorker.printList(Circle.class);
        System.out.println("Cone list:");
        fileWorker.printList(Cone.class);

        //do tasks
        doTask(Circle.class);
        doTask(Cone.class);

        //save data to file
        //storage.save(MAIN_FILE_NAME);
        if(flag) fileWorker.serializeFastJSON(MAIN_FILE_NAME);
        System.out.println("Data saved to file '" + MAIN_FILE_NAME + "'");

        System.out.println("\nJSON serialization:");
        fileWorker.serialize("json-" +MAIN_FILE_NAME);
        fileWorker.deserialize("json-" +MAIN_FILE_NAME);
        System.out.println("Circle list:");
        fileWorker.printList(Circle.class);
        System.out.println("Cone list:");
        fileWorker.printList(Cone.class);
    }

    public static void doTask(Class<?> figureClass)
    {
        if(figureClass == Circle.class)
        {
            System.out.println("Processing the Circle...");
            List<Circle> list = fileWorker.getFigureList(figureClass);
            if(list != null)
            {
                float averageCircle = 0;
                for (Circle tr : list)
                {
                    averageCircle += tr.getArea();
                }
                averageCircle /= list.size();
                System.out.println("Average square equals " + averageCircle);

                int lowerSquareValuesCount = 0;
                for (Circle tr : list)
                {
                    if (tr.getArea() < averageCircle) lowerSquareValuesCount++;
                }
                System.out.println("The amount of Circle with lower square values equals " + lowerSquareValuesCount + "\n");
            }
        }
        else if(figureClass == Cone.class)
        {
            System.out.println("Processing the Cone...");
            List<Cone> list = fileWorker.getFigureList(figureClass);
            if(list != null)
            {
                double maxConeValume = 0;
                int coneNumber = 1;
                for(Cone cone : list){
                    if(maxConeValume < cone.GetVolume()) {
                        maxConeValume = cone.GetVolume();
                        coneNumber++;
                    }
                }
                System.out.println("Cone numbered " + coneNumber + " has the largest area: " + maxConeValume);
            }
        }
        else
        {
            System.out.println("Error! No task for class <" + figureClass.toString() + ">");
        }
    }

    public static int readInteger()
    {
        return readInteger(1, 9999);
    }

    public static int readInteger(int minValue, int maxValue)
    {
        Scanner input = new Scanner(System.in);
        while(true)
        {
            try
            {
                int result = Integer.parseInt(input.next());
                if(result >= minValue)
                {
                    if(result <= maxValue) return result;
                    else
                    {
                        System.out.println("Value must be less than " + (maxValue + 1));
                    }
                }
                else
                {
                    System.out.println("Value must be greater than " + (minValue - 1));
                }
            }
            catch (Exception e)
            {
                System.out.println("Enter a number, please");
            }
        }
    }
}
