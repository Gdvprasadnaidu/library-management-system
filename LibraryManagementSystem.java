import javax.swing.*;
import javax.swing.text.DefaultHighlighter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class LibraryManagementSystem {
    private static List<Book> books = new ArrayList<>();
    private static final String filePath = "db.txt";
    private static final String studentDbFilePath = "studentdb.txt";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JLabel label = new JLabel("Library Management System");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton adminButton = new JButton("Admin");
        JButton userButton = new JButton("User");

        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog("Admin", "x", "x", "Admin Page");
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog("User", "user", "vertex26", "User Page");
            }
        });

        frame.add(label, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showLoginDialog(String userType, String expectedID, String expectedPassword, String pageName) {
        JDialog loginDialog = new JDialog();
        loginDialog.setTitle(userType + " Login");
        loginDialog.setSize(300, 150);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setLayout(new GridLayout(4, 2));

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginDialog.add(idLabel);
        loginDialog.add(idField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(new JLabel());
        loginDialog.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredID = idField.getText();
                String enteredPassword = new String(passwordField.getPassword());

                if (enteredID.equals(expectedID) && enteredPassword.equals(expectedPassword)) {
                    loginDialog.dispose();
                    showPage(pageName);
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid ID or Password");
                }
            }
        });

        loginDialog.setVisible(true);
    }

    private static void showPage(String pageName) {
        JFrame pageFrame = new JFrame(pageName);
        pageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pageFrame.setLocationRelativeTo(null);
        pageFrame.setSize(300, 400);
/*---------------------------------------------------------------------------------------------------------------------------------- */
if (pageName.equals("Admin Page")) {
    JPanel adminPanel = new JPanel();
    adminPanel.setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new GridLayout(0, 2));

    JTextField bookNameField = new JTextField(10);
    JTextField authorNameField = new JTextField(10);

    JButton addBookButton = new JButton("Add");
    JButton deleteBookButton = new JButton("Delete");

    JPanel bottomButtonPanel = new JPanel();
    bottomButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    JButton saveButton = new JButton("Save");
    JButton importButton = new JButton("Import");

    bottomButtonPanel.add(saveButton);
    bottomButtonPanel.add(importButton);

    inputPanel.add(new JLabel("Book Name:"));
    inputPanel.add(bookNameField);
    inputPanel.add(new JLabel("Author Name:"));
    inputPanel.add(authorNameField);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(0, 1));

    buttonPanel.add(addBookButton);
    buttonPanel.add(deleteBookButton);

    JPanel containerPanel = new JPanel();
    containerPanel.setLayout(new BorderLayout());
    containerPanel.add(inputPanel, BorderLayout.WEST);
    containerPanel.add(buttonPanel, BorderLayout.CENTER);

    JPanel bookDisplayPanel = new JPanel();
bookDisplayPanel.setLayout(new BoxLayout(bookDisplayPanel, BoxLayout.Y_AXIS));

addBookButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String bookName = bookNameField.getText();
        String authorName = authorNameField.getText();

        if (!bookName.isEmpty() && !authorName.isEmpty()) {
            Book book = new Book(bookName, authorName);
            books.add(book);
            bookNameField.setText("");
            authorNameField.setText("");

            updateBookList(bookDisplayPanel);
        } else {
            JOptionPane.showMessageDialog(adminPanel, "Please enter book name and author name.");
        }
    }
});

    
    deleteBookButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String bookNameToDelete = bookNameField.getText();
            if (!bookNameToDelete.isEmpty()) {
                boolean bookDeleted = false;
                for (Book book : books) {
                    if (book.getName().equalsIgnoreCase(bookNameToDelete)) {
                        books.remove(book);
                        bookNameField.setText("");
                        authorNameField.setText("");
                        updateBookList(bookDisplayPanel);
                        saveBooksToFile();
                        bookDeleted = true;
                        break;
                    }
                }

                if (bookDeleted) {
                    JOptionPane.showMessageDialog(adminPanel, "Deleted");
                } else {
                    JOptionPane.showMessageDialog(adminPanel, "Book not found.");
                }
            } else {
                JOptionPane.showMessageDialog(adminPanel, "Please enter the book name to delete.");
            }
        }
    });

    saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveDisplayedBooksToFile(bookDisplayPanel);
            JOptionPane.showMessageDialog(adminPanel, "Books saved successfully.");

            pageFrame.dispose();
        }
    });

    importButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame studentDetailsFrame = new JFrame("Student Details");
            studentDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            studentDetailsFrame.setSize(500, 400);
            studentDetailsFrame.setLocationRelativeTo(null);

            JPanel studentDetailsPanel = new JPanel();
            studentDetailsPanel.setLayout(new BorderLayout());

            JTextArea studentDetailsTextArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(studentDetailsTextArea);
            studentDetailsPanel.add(scrollPane, BorderLayout.CENTER);

            JTextField searchField = new JTextField();
            JButton searchButton = new JButton("Search");

            JPanel searchPanel = new JPanel();
            searchPanel.setLayout(new BorderLayout());
            searchPanel.add(searchField, BorderLayout.CENTER);
            searchPanel.add(searchButton, BorderLayout.EAST);
            studentDetailsPanel.add(searchPanel, BorderLayout.NORTH);

            List<String> studentDetailsList = getDataFromFile(studentDbFilePath);
            for (String details : studentDetailsList) {
                studentDetailsTextArea.append(details + "\n");
            }

            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String searchText = searchField.getText().trim();
                    highlightStudentName(studentDetailsTextArea, searchText);
                }
            });

            studentDetailsFrame.add(studentDetailsPanel);

            studentDetailsFrame.setVisible(true);
        }
    });


    adminPanel.add(containerPanel, BorderLayout.NORTH);
    adminPanel.add(bookDisplayPanel, BorderLayout.CENTER);
    adminPanel.add(bottomButtonPanel, BorderLayout.SOUTH);

    pageFrame.add(adminPanel, BorderLayout.WEST);
    pageFrame.setLocationRelativeTo(null);
}


/*------------------------------------------------------------------------------------------------------------------------------------- */
if (pageName.equals("User Page")) {
    JPanel userPanel = new JPanel();
    userPanel.setLayout(new BorderLayout());

    JLabel nameLabel = new JLabel("Student Name:");
    JTextField studentNameField = new JTextField(10);  

    JPanel studentNamePanel = new JPanel();
    studentNamePanel.setLayout(new FlowLayout());
    studentNamePanel.add(nameLabel);
    studentNamePanel.add(studentNameField);
    userPanel.add(studentNamePanel, BorderLayout.NORTH); 

    JPanel checkboxPanel = new JPanel();
    checkboxPanel.setLayout(new GridLayout(0, 1)); 

    // Add checkboxes for each line
    List<JCheckBox> checkBoxList = new ArrayList<>();

    for (String line : getDataFromFile()) {
        JCheckBox checkBox = new JCheckBox(line);
        checkBoxList.add(checkBox);
        checkboxPanel.add(checkBox);  // Add checkboxes to the checkboxPanel
    }

    // Wrap the checkbox panel in a JScrollPane
    JScrollPane checkboxScrollPane = new JScrollPane(checkboxPanel);

    // Create a panel for buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    // Add Save button
    JButton saveButton = new JButton("Save");
    buttonPanel.add(saveButton);

    // Add Instruction button
    JButton instructionButton = new JButton("Instructions");
    buttonPanel.add(instructionButton);

    // Add checkboxScrollPane below the buttons
    userPanel.add(buttonPanel, BorderLayout.SOUTH);
    userPanel.add(checkboxScrollPane, BorderLayout.CENTER);  // Use CENTER for checkboxes

    // Save button ActionListener
    saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> selectedData = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxList) {
                if (checkBox.isSelected()) {
                    selectedData.add(checkBox.getText());
                }
            }

            // Check if at least one checkbox is selected
            if (!selectedData.isEmpty() && !studentNameField.getText().isEmpty()) {
                // Save data to studentdb.txt
                saveStudentData(studentNameField.getText(), selectedData);
                JOptionPane.showMessageDialog(userPanel, "Data saved successfully.");
                
            } else {
                JOptionPane.showMessageDialog(userPanel, "Please select data and enter student name before saving.");
                
            }
        }
    });

    instructionButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Add your instructions here
            JOptionPane.showMessageDialog(userPanel, "Instructions:\n1. Select data using radio buttons.\n2. Enter student name.\n3. Click 'Save' to store the information.\n4. Make sure your selection should be perfect before saving.");
        }
    });

    pageFrame.add(userPanel, BorderLayout.NORTH);
    pageFrame.setSize(400, 470);  // Adjust the size if needed
}


/*------------------------------------------------------------------------------------------------------------------------------------ */

pageFrame.setLocationRelativeTo(null);
pageFrame.setVisible(true);
}

private static List<String> getDataFromFile() {
    List<String> data = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                data.add(line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        // Handle the exception as needed
    }

    return data;
}



private static void saveStudentData(String studentName, List<String> selectedData) {
    String filePath = studentDbFilePath;

    try (FileWriter fileWriter = new FileWriter(filePath, true);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

        printWriter.print("Student Name: " + studentName + ", Selected Data: ");
        for (String data : selectedData) {
            printWriter.print(data + ", ");
        }
        printWriter.println();

    } catch (IOException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
}


private static void updateBookList(JPanel bookDisplayPanel) {
    // Clear existing components in the bookDisplayPanel
    bookDisplayPanel.removeAll();

    // Set a BoxLayout for the bookDisplayPanel
    bookDisplayPanel.setLayout(new BoxLayout(bookDisplayPanel, BoxLayout.Y_AXIS));

    // Display all existing books in the books list
    for (Book book : books) {
        JLabel bookLabel = new JLabel("Book: " + book.getName() + " | Author: " + book.getAuthor());
        bookDisplayPanel.add(bookLabel);
    }

    // Revalidate and repaint the bookDisplayPanel
    bookDisplayPanel.revalidate();
    bookDisplayPanel.repaint();
}



private static void saveDisplayedBooksToFile(JPanel bookDisplayPanel) {
    try (FileWriter fileWriter = new FileWriter(filePath, true);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

        // Iterate over the displayed book details and save them to the file
        Component[] components = bookDisplayPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                String bookDetails = label.getText().replace("<html>", "").replace("</html>", "") + "\n";
                printWriter.println(bookDetails);
            }
        }

    } catch (IOException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
}





private static void saveBooksToFile() {
    try (FileWriter fileWriter = new FileWriter(filePath, true);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

        for (Book savedBook : books) {
            // Format the data before saving to the file
            printWriter.println("book: " + savedBook.getName() + ", author: " + savedBook.getAuthor());
        }
    } catch (IOException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
}

private static void highlightStudentName(JTextArea textArea, String searchText) {
        // Clear previous highlights
        textArea.getHighlighter().removeAllHighlights();

        String text = textArea.getText();
        int index = text.indexOf(searchText);

        while (index >= 0) {
            int endIndex = index + searchText.length();

            try {
                // Highlight the student name in red
                textArea.getHighlighter().addHighlight(index, endIndex, new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Find the next occurrence
            index = text.indexOf(searchText, endIndex);
        }
    }

    private static List<String> getDataFromFile(String filePath) {
        List<String> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return data;
    }

    static class Book {
        private String name;
        private String author;

        public Book(String name, String author) {
            this.name = name;
            this.author = author;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}


