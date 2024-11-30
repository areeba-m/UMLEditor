//package ui;
//
//import BusinessLayer.Components.ClassDiagramComponents.ClassBox;
//import BusinessLayer.Components.UseCaseDiagramComponents.UseCase;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.dnd.*;
//
//public class DrawingCanvas extends JPanel implements DropTargetListener {
//
//    public DrawingCanvas(){
//        setLayout(null);
//        setPreferredSize(new Dimension(2000,2000));
//        setBackground(Color.PINK);
//
//        new DropTarget(this,this);
//    }
//
//    @Override
//    public void drop(DropTargetDropEvent event) {
//        try {
//            event.acceptDrop(DnDConstants.ACTION_COPY);
//                // Get drop location
//                Point dropPoint = event.getLocation();
//                System.out.println("Drag and drop x: "+dropPoint.x + ", y: " + dropPoint.y);
//                // Create a new ClassBox and set its bounds
//                ClassBox classbox = new ClassBox("Class", "Interface"); // Adjust with actual type
//                classbox.setBounds(dropPoint.x, dropPoint.y, 200, 200);
//                classbox.setLocation(dropPoint);
//               // classbox.getPoint().setLocation(dropPoint);
//                // Add the new component
//                add(classbox);
//
//                // Refresh panel
//                revalidate();
//                repaint();
//        } catch (Exception e) {
//            // display error
//        }
//    }
//
//    @Override
//    public void dragEnter(DropTargetDragEvent dtde) {
//
//    }
//
//    @Override
//    public void dragOver(DropTargetDragEvent dtde) {
//
//    }
//
//    @Override
//    public void dropActionChanged(DropTargetDragEvent dtde) {
//
//    }
//
//    @Override
//    public void dragExit(DropTargetEvent dte) {
//
//    }
//}