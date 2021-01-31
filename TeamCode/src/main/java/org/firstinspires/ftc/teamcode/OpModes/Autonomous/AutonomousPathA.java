package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.CoordinateConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import static org.firstinspires.ftc.teamcode.OpModes.Autonomous.AutonomousFramework.CLAW_CLOSE_POS;
import static org.firstinspires.ftc.teamcode.OpModes.Autonomous.AutonomousFramework.CLAW_OPEN_POS;

public class AutonomousPathA {

    public static int armPos = -525;
    public static int armPos2 = -500;

    public static int testX = -37;
    public static int testY = 55;

    Pose2d shootingPose = new Pose2d(0, 24, Math.toRadians(0.0));
    Pose2d placeGoalPose = new Pose2d(0, 48, Math.toRadians(-10.0));

    Pose2d pickUpGoalPose1 = new Pose2d(-24, testY, Math.toRadians(180.0));
    Pose2d pickUpGoalPose2 = new Pose2d(testX, testY, Math.toRadians(180.0));
    Pose2d placeSecondGoalPose = new Pose2d(0, 57, Math.toRadians(0.0));
    Pose2d parkingPose = new Pose2d(10, 30, Math.toRadians(0.0));

    public void followPath(SampleMecanumDrive drive, DcMotor armMotor, Servo clawServo) {
        Trajectory goToShootingandPlaceGoalPose = drive.trajectoryBuilder(CoordinateConstants.START_POS_BLUE_2)
                .splineTo(placeGoalPose.vec(), placeGoalPose.getHeading())
                .addDisplacementMarker(() -> {
                    armMotor.setTargetPosition(armPos);
                    armMotor.setPower(0.3);
                    sleep(500);
                })
                .build();

        Trajectory goToPickUpGoalPose1 = drive.trajectoryBuilder(goToShootingandPlaceGoalPose.end())
                .addDisplacementMarker(()->{
                    clawServo.setPosition(CLAW_OPEN_POS);
                    armMotor.setTargetPosition(-100);
                    armMotor.setPower(0.3);
                    sleep(500);
                })
                .lineToLinearHeading(pickUpGoalPose1)
                .build();

        Trajectory goToPickUpGoalPose2 = drive.trajectoryBuilder(goToPickUpGoalPose1.end())
                .addDisplacementMarker(() -> {
                    armMotor.setTargetPosition(armPos2);
                    armMotor.setPower(0.3);
                    sleep(500);
                })
                .lineToConstantHeading(pickUpGoalPose2.vec())
                .build();


        Trajectory goToPlaceSecondGoalPart1 = drive.trajectoryBuilder(goToPickUpGoalPose2.end())
                .addDisplacementMarker(() -> {
//                    armMotor.setTargetPosition(armPos2);
//                    armMotor.setPower(0.3);
//                    sleep(500);
                })
                .lineToSplineHeading(placeSecondGoalPose)
                .build();

        Trajectory goToParking = drive.trajectoryBuilder(goToPlaceSecondGoalPart1.end())
                .lineToConstantHeading(parkingPose.vec())
                .build();


        drive.followTrajectory(goToShootingandPlaceGoalPose);

        sleep(1000);

        drive.followTrajectory(goToPickUpGoalPose1);

        sleep(1000);

        drive.followTrajectory(goToPickUpGoalPose2);

        clawServo.setPosition(CLAW_CLOSE_POS);

        sleep(1000);

        drive.followTrajectory(goToPlaceSecondGoalPart1);

        clawServo.setPosition(CLAW_OPEN_POS);

        drive.followTrajectory(goToParking);

        sleep(1000);

        armMotor.setTargetPosition(armPos);
        armMotor.setPower(0.3);

    }

    // sleep method from LinearOpMode
    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}