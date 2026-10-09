import java.io.*;
import java.net.*;
import java.util.Locale;

public class Server
{
    static final int PORT = 5000;

    public static void main(String[] args)
    {
        BufferedReader br =
            new BufferedReader(
                new InputStreamReader(System.in)
            );

        try
        {
            ServerSocket serverSocket =
                new ServerSocket(PORT);

            System.out.println("Server started...");
            System.out.println(
                "Waiting for client connection..."
            );

            while (true)
            {
                Socket socket =
                    serverSocket.accept();

                System.out.println();
                System.out.println(
                    "Client connected."
                );

                boolean clientConnected =
                    handleClient(socket);

                socket.close();

                System.out.println();

                if (!clientConnected)
                {
                    System.out.println(
                        "Client disconnected."
                    );
                }

                System.out.print(
                    "Do you want to keep the server running " +
                    "for another client? (y/n): "
                );

                String answer =
                    br.readLine();

                if (
                    answer == null ||
                    !answer.equalsIgnoreCase("y")
                )
                {
                    System.out.println(
                        "Server shutting down..."
                    );

                    break;
                }

                System.out.println();
                System.out.println(
                    "Server is still running."
                );

                System.out.println(
                    "Waiting for another client..."
                );
            }

            serverSocket.close();

            System.out.println(
                "Server closed."
            );
        }
        catch (Exception e)
        {
            System.out.println(
                "Error: " + e.getMessage()
            );
        }
    }


    // ==================================================
    // HANDLE CLIENT
    // ==================================================

    static boolean handleClient(
        Socket socket
    )
    {
        try
        {
            DataInputStream is =
                new DataInputStream(
                    socket.getInputStream()
                );

            DataOutputStream os =
                new DataOutputStream(
                    socket.getOutputStream()
                );

            while (true)
            {
                String request;

                try
                {
                    request =
                        is.readUTF();
                }
                catch (EOFException e)
                {
                    /*
                     * Client closed the connection.
                     */
                    return false;
                }

                // ==============================
                // CHAT
                // ==============================

                if (request.equals("CHAT"))
                {
                    chat(is, os);
                }

                // ==============================
                // ARITHMETIC
                // ==============================

                else if (
                    request.equals("ARITHMETIC")
                )
                {
                    arithmetic(is, os);
                }

                // ==============================
                // TRIGONOMETRY
                // ==============================

                else if (
                    request.equals("TRIGONOMETRY")
                )
                {
                    trigonometry(is, os);
                }

                // ==============================
                // FILE UPLOAD
                // ==============================

                else if (
                    request.equals("UPLOAD")
                )
                {
                    receiveFile(is, os);
                }

                // ==============================
                // FILE DOWNLOAD
                // ==============================

                else if (
                    request.equals("DOWNLOAD")
                )
                {
                    sendFile(is, os);
                }

                // ==============================
                // CLIENT EXIT
                // ==============================

                else if (
                    request.equals("EXIT")
                )
                {
                    System.out.println();
                    System.out.println(
                        "Client selected Exit."
                    );

                    return true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(
                "Connection ended."
            );

            return false;
        }
    }


    // ==================================================
    // CHAT
    // ==================================================

    static void chat(
        DataInputStream is,
        DataOutputStream os
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== CHAT ====="
        );

        System.out.println(
            "Chat started."
        );

        System.out.println(
            "Type 'exit' to end chat."
        );

        BufferedReader br =
            new BufferedReader(
                new InputStreamReader(System.in)
            );

        while (true)
        {
            String clientMessage;

            try
            {
                clientMessage =
                    is.readUTF();
            }
            catch (EOFException e)
            {
                System.out.println(
                    "Client disconnected."
                );

                return;
            }

            System.out.println(
                "Client: " +
                clientMessage
            );

            if (
                clientMessage.equalsIgnoreCase(
                    "exit"
                )
            )
            {
                os.writeUTF(
                    "CHAT_END"
                );

                os.flush();

                System.out.println(
                    "Client ended the chat."
                );

                break;
            }

            System.out.print(
                "Server: "
            );

            String serverMessage =
                br.readLine();

            if (serverMessage == null)
            {
                serverMessage = "exit";
            }

            os.writeUTF(
                serverMessage
            );

            os.flush();

            if (
                serverMessage.equalsIgnoreCase(
                    "exit"
                )
            )
            {
                System.out.println(
                    "Server ended the chat."
                );

                break;
            }
        }

        System.out.println(
            "Returning to client menu..."
        );
    }


    // ==================================================
    // ARITHMETIC CALCULATOR
    // ==================================================

    static void arithmetic(
        DataInputStream is,
        DataOutputStream os
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== ARITHMETIC CALCULATOR ====="
        );

        int operation =
            is.readInt();

        double num1 =
            is.readDouble();

        double num2 =
            is.readDouble();

        double result;

        String operationName;

        if (operation == 1)
        {
            result =
                num1 + num2;

            operationName =
                "Addition";
        }
        else if (operation == 2)
        {
            result =
                num1 - num2;

            operationName =
                "Subtraction";
        }
        else if (operation == 3)
        {
            result =
                num1 * num2;

            operationName =
                "Multiplication";
        }
        else if (operation == 4)
        {
            operationName =
                "Division";

            if (num2 == 0)
            {
                os.writeUTF(
                    "ERROR: Cannot divide by zero."
                );

                os.flush();

                System.out.println(
                    "Division by zero requested."
                );

                return;
            }

            result =
                num1 / num2;
        }
        else
        {
            os.writeUTF(
                "ERROR: Invalid operation."
            );

            os.flush();

            return;
        }

        String resultText =
            String.format(
                Locale.US,
                "%.4f",
                result
            );

        System.out.println(
            "Operation: " +
            operationName
        );

        System.out.println(
            "Calculation: " +
            num1 +
            " " +
            getArithmeticSymbol(operation) +
            " " +
            num2 +
            " = " +
            resultText
        );

        os.writeUTF(
            "RESULT:" + resultText
        );

        os.flush();
    }


    // ==================================================
    // ARITHMETIC SYMBOL
    // ==================================================

    static String getArithmeticSymbol(
        int operation
    )
    {
        if (operation == 1)
        {
            return "+";
        }
        else if (operation == 2)
        {
            return "-";
        }
        else if (operation == 3)
        {
            return "*";
        }
        else
        {
            return "/";
        }
    }


    // ==================================================
    // TRIGONOMETRY
    // ==================================================

    static void trigonometry(
        DataInputStream is,
        DataOutputStream os
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== TRIGONOMETRY CALCULATOR ====="
        );

        int operation =
            is.readInt();

        double angle =
            is.readDouble();

        double radians =
            Math.toRadians(angle);

        double result;

        String operationName;

        if (operation == 1)
        {
            result =
                Math.sin(radians);

            operationName =
                "sin";
        }
        else if (operation == 2)
        {
            result =
                Math.cos(radians);

            operationName =
                "cos";
        }
        else if (operation == 3)
        {
            result =
                Math.tan(radians);

            operationName =
                "tan";
        }
        else
        {
            os.writeUTF(
                "ERROR: Invalid operation."
            );

            os.flush();

            return;
        }

        /*
         * Remove very small floating-point
         * errors near zero.
         */
        if (Math.abs(result) < 0.0000000001)
        {
            result = 0;
        }

        /*
         * Values such as tan(45) can become
         * 0.9999999999999999 because of floating
         * point representation.
         */
        if (Math.abs(result - 1.0) < 0.0000000001)
        {
            result = 1.0;
        }

        String resultText =
            String.format(
                Locale.US,
                "%.4f",
                result
            );

        System.out.println(
            "Operation: " +
            operationName
        );

        System.out.println(
            "Angle: " +
            angle +
            " degrees"
        );

        System.out.println(
            "Result: " +
            resultText
        );

        os.writeUTF(
            "RESULT:" +
            resultText
        );

        os.flush();
    }


    // ==================================================
    // FILE UPLOAD
    // ==================================================

    static void receiveFile(
        DataInputStream is,
        DataOutputStream os
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== FILE UPLOAD ====="
        );

        File folder =
            new File("server_files");

        if (!folder.exists())
        {
            folder.mkdir();
        }

        String fileName =
            is.readUTF();

        long fileSize =
            is.readLong();

        System.out.println(
            "Receiving file: " +
            fileName
        );

        System.out.println(
            "File size: " +
            fileSize +
            " bytes"
        );

        File file =
            new File(
                folder,
                fileName
            );

        FileOutputStream fos =
            new FileOutputStream(file);

        byte[] buffer =
            new byte[4096];

        long remaining =
            fileSize;

        while (remaining > 0)
        {
            int bytesToRead =
                (int)Math.min(
                    buffer.length,
                    remaining
                );

            int bytesRead =
                is.read(
                    buffer,
                    0,
                    bytesToRead
                );

            if (bytesRead == -1)
            {
                throw new IOException(
                    "File transfer interrupted."
                );
            }

            fos.write(
                buffer,
                0,
                bytesRead
            );

            remaining -= bytesRead;
        }

        fos.close();

        System.out.println(
            "File uploaded successfully."
        );

        os.writeUTF(
            "UPLOAD_SUCCESS"
        );

        os.flush();
    }


    // ==================================================
    // FILE DOWNLOAD
    // ==================================================

    static void sendFile(
        DataInputStream is,
        DataOutputStream os
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== FILE DOWNLOAD ====="
        );

        File folder =
            new File("server_files");

        if (!folder.exists())
        {
            folder.mkdir();
        }

        File[] files =
            folder.listFiles();

        if (
            files == null ||
            files.length == 0
        )
        {
            os.writeUTF(
                "NO_FILES"
            );

            os.flush();

            System.out.println(
                "No files available on server."
            );

            return;
        }

        int fileCount = 0;

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isFile())
            {
                fileCount++;
            }
        }

        if (fileCount == 0)
        {
            os.writeUTF(
                "NO_FILES"
            );

            os.flush();

            return;
        }

        os.writeUTF(
            "FILE_LIST"
        );

        os.writeInt(
            fileCount
        );

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isFile())
            {
                os.writeUTF(
                    files[i].getName()
                );
            }
        }

        os.flush();

        int choice =
            is.readInt();

        File selectedFile =
            null;

        int currentNumber = 0;

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isFile())
            {
                currentNumber++;

                if (currentNumber == choice)
                {
                    selectedFile =
                        files[i];

                    break;
                }
            }
        }

        if (selectedFile == null)
        {
            os.writeUTF(
                "FILE_ERROR"
            );

            os.flush();

            return;
        }

        os.writeUTF(
            "FILE_START"
        );

        os.writeUTF(
            selectedFile.getName()
        );

        os.writeLong(
            selectedFile.length()
        );

        FileInputStream fis =
            new FileInputStream(
                selectedFile
            );

        byte[] buffer =
            new byte[4096];

        int bytesRead;

        while (
            (bytesRead =
                fis.read(buffer)) != -1
        )
        {
            os.write(
                buffer,
                0,
                bytesRead
            );
        }

        os.flush();

        fis.close();

        System.out.println(
            "File sent: " +
            selectedFile.getName()
        );
    }
}