package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import jobs.PatientsDeleteJob;
import jobs.PatientsLockJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class Main extends Application {

    @Override
    public void start(Stage loginStage) {
        executeLoginWindow();
    }

    public void executeLoginWindow() {
        try {
            createLoginWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLoginWindow() throws IOException {
        ControllerManager.getInstance().getLoginStage().show();
    }

    public static void main(String[] args) {
        // Launching Quartz Cronjobs
        launchCronJobs();
        // Initializing & Launching JavaFX
        launch(args);
    }

    /**
     * Launches all Cronjobs of Application
     */
    private static void launchCronJobs() {
        // Building the Job Details for the Jobs
        JobDetail patientsLockJob = newJob(PatientsLockJob.class)
                .withIdentity("lockJob", "patient")
                .build();
        JobDetail patientsDeleteJob = newJob(PatientsDeleteJob.class)
                .withIdentity("deleteJob", "patient")
                .build();


        // Building Cron Triggers for the Jobs
        Trigger patientsLockTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("lockTrigger", "patient")
                .withDescription("Checks every 10 Minutes if any Patient has to be locked")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInMinutes(10)
                        .repeatForever()
                )
                .build();
        Trigger patientsDeleteTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("deleteTrigger", "patient")
                .withDescription("Checks every Hour if any Patient has to be deleted")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever()
                )
                .build();

        try {
            // Starting the Scheduler and Scheduling the Triggers
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(patientsLockJob, patientsLockTrigger);
            scheduler.scheduleJob(patientsDeleteJob, patientsDeleteTrigger);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
    }
}