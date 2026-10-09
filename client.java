import java.io.*;
import java.net.*;

public class Client
{
    static final int PORT = 5000;

    public static void main(String[] args)
    {
        String serverIP =
            "localhost";

        try
        {
            Socket socket =
                new Socket(
                    serverIP,
                    PORT
                );

            System.out.println(
                "Connected to server."
            );

            DataInputStream is =
                new DataInputStream(
                    socket.getInputStream()
                );

            DataOutputStream os =
                new DataOutputStream(
                    socket.getOutputStream()
                );

            BufferedReader br =
                new BufferedReader(
                    new InputStreamReader(
                        System.in
                    )
                );

            while (true)
            {
                showMenu();

                String input =
                    br.readLine();

                int choice;

                try
                {
                    choice =
                        Integer.parseInt(input);
                }
                catch (Exception e)
                {
                    System.out.println(
                        "Please enter a valid choice."
                    );

                    continue;
                }

                if (choice == 1)
                {
                    os.writeUTF("CHAT");
                    os.flush();

                    chat(
                        is,
                        os,
                        br
                    );
                }

                else if (choice == 2)
                {
                    os.writeUTF("ARITHMETIC");
                    os.flush();

                    arithmetic(
                        is,
                        os,
                        br
                    );
                }

                else if (choice == 3)
                {
                    os.writeUTF("TRIGONOMETRY");
                    os.flush();

                    trigonometry(
                        is,
                        os,
                        br
                    );
                }

                else if (choice == 4)
                {
                    os.writeUTF("UPLOAD");
                    os.flush();

                    uploadFile(
                        is,
                        os,
                        br
                    );
                }

                else if (choice == 5)
                {
                    os.writeUTF("DOWNLOAD");
                    os.flush();

                    downloadFile(
                        is,
                        os,
                        br
                    );
                }

                else if (choice == 6)
                {
                    os.writeUTF("EXIT");
                    os.flush();

                    System.out.println(
                        "Closing connection..."
                    );

                    break;
                }

                else
                {
                    System.out.println(
                        "Invalid choice."
                    );
                }
            }

            socket.close();

            System.out.println(
                "Client closed."
            );
        }
        catch (Exception e)
        {
            System.out.println(
                "Error: " +
                e.getMessage()
            );
        }
    }


    // ==================================================
    // MENU
    // ==================================================

    static void showMenu()
    {
        System.out.println();
        System.out.println(
            "----- MENU -----"
        );

        System.out.println(
            "1. Chat"
        );

        System.out.println(
            "2. Arithmetic Calculator"
        );

        System.out.println(
            "3. Trigonometry Calculator"
        );

        System.out.println(
            "4. Upload File"
        );

        System.out.println(
            "5. Download File"
        );

        System.out.println(
            "6. Exit"
        );

        System.out.print(
            "Enter your choice: "
        );
    }


    // ==================================================
    // CHAT
    // ==================================================

    static void chat(
        DataInputStream is,
        DataOutputStream os,
        BufferedReader br
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

        while (true)
        {
            System.out.print(
                "Client: "
            );

            String clientMessage =
                br.readLine();

            os.writeUTF(
                clientMessage
            );

            os.flush();

            if (
                clientMessage.equalsIgnoreCase(
                    "exit"
                )
            )
            {
                String response =
                    is.readUTF();

                if (
                    response.equals(
                        "CHAT_END"
                    )
                )
                {
                    System.out.println(
                        "Chat ended."
                    );
                }

                break;
            }

            String serverMessage =
                is.readUTF();

            if (
                serverMessage.equals(
                    "CHAT_END"
                )
            )
            {
                System.out.println(
                    "Server ended the chat."
                );

                break;
            }

            System.out.println(
                "Server: " +
                serverMessage
            );

            if (
                serverMessage.equalsIgnoreCase(
                    "exit"
                )
            )
            {
                System.out.println(
                    "Chat ended."
                );

                break;
            }
        }

        System.out.println(
            "Returning to menu..."
        );
    }


    // ==================================================
    // ARITHMETIC
    // ==================================================

    static void arithmetic(
        DataInputStream is,
        DataOutputStream os,
        BufferedReader br
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== ARITHMETIC CALCULATOR ====="
        );

        System.out.println(
            "1. Addition"
        );

        System.out.println(
            "2. Subtraction"
        );

        System.out.println(
            "3. Multiplication"
        );

        System.out.println(
            "4. Division"
        );

        System.out.print(
            "Enter operation: "
        );

        int operation =
            Integer.parseInt(
                br.readLine()
            );

        System.out.print(
            "Enter first number: "
        );

        double num1 =
            Double.parseDouble(
                br.readLine()
            );

        System.out.print(
            "Enter second number: "
        );

        double num2 =
            Double.parseDouble(
                br.readLine()
            );

        os.writeInt(
            operation
        );

        os.writeDouble(
            num1
        );

        os.writeDouble(
            num2
        );

        os.flush();

        String response =
            is.readUTF();

        if (
            response.startsWith(
                "RESULT:"
            )
        )
        {
            System.out.println();
            System.out.println(
                "Result = " +
                response.substring(7)
            );
        }
        else
        {
            System.out.println(
                response
            );
        }

        System.out.println(
            "Returning to menu..."
        );
    }


    // ==================================================
    // TRIGONOMETRY
    // ==================================================

    static void trigonometry(
        DataInputStream is,
        DataOutputStream os,
        BufferedReader br
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== TRIGONOMETRY CALCULATOR ====="
        );

        System.out.println(
            "1. Sin"
        );

        System.out.println(
            "2. Cos"
        );

        System.out.println(
            "3. Tan"
        );

        System.out.print(
            "Enter operation: "
        );

        int operation =
            Integer.parseInt(
                br.readLine()
            );

        System.out.print(
            "Enter angle in degrees: "
        );

        double angle =
            Double.parseDouble(
                br.readLine()
            );

        os.writeInt(
            operation
        );

        os.writeDouble(
            angle
        );

        os.flush();

        String response =
            is.readUTF();

        if (
            response.startsWith(
                "RESULT:"
            )
        )
        {
            System.out.println();
            System.out.println(
                "Result = " +
                response.substring(7)
            );
        }
        else
        {
            System.out.println(
                response
            );
        }

        System.out.println(
            "Returning to menu..."
        );
    }


    // ==================================================
    // FILE UPLOAD
    // ==================================================

    static void uploadFile(
        DataInputStream is,
        DataOutputStream os,
        BufferedReader br
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== FILE UPLOAD ====="
        );

        System.out.print(
            "Enter file path: "
        );

        String filePath =
            br.readLine();

        File file =
            new File(filePath);

        if (!file.exists())
        {
            System.out.println(
                "File not found."
            );

            return;
        }

        if (!file.isFile())
        {
            System.out.println(
                "The given path is not a file."
            );

            return;
        }

        System.out.println(
            "Uploading: " +
            file.getName()
        );

        os.writeUTF(
            file.getName()
        );

        os.writeLong(
            file.length()
        );

        FileInputStream fis =
            new FileInputStream(file);

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

        String response =
            is.readUTF();

        if (
            response.equals(
                "UPLOAD_SUCCESS"
            )
        )
        {
            System.out.println();
            System.out.println(
                "File uploaded successfully."
            );
        }

        System.out.println(
            "Returning to menu..."
        );
    }


    // ==================================================
    // FILE DOWNLOAD
    // ==================================================

    static void downloadFile(
        DataInputStream is,
        DataOutputStream os,
        BufferedReader br
    ) throws Exception
    {
        System.out.println();
        System.out.println(
            "===== FILE DOWNLOAD ====="
        );

        System.out.println(
            "Getting files from server..."
        );

        String response =
            is.readUTF();

        if (
            response.equals(
                "NO_FILES"
            )
        )
        {
            System.out.println(
                "No files are available on server."
            );

            return;
        }

        if (
            !response.equals(
                "FILE_LIST"
            )
        )
        {
            System.out.println(
                "Unexpected server response."
            );

            return;
        }

        int fileCount =
            is.readInt();

        System.out.println();
        System.out.println(
            "Files available on server:"
        );

        for (
            int i = 1;
            i <= fileCount;
            i++
        )
        {
            String fileName =
                is.readUTF();

            System.out.println(
                i +
                ". " +
                fileName
            );
        }

        System.out.print(
            "Enter file number: "
        );

        int choice =
            Integer.parseInt(
                br.readLine()
            );

        os.writeInt(
            choice
        );

        os.flush();

        response =
            is.readUTF();

        if (
            response.equals(
                "FILE_ERROR"
            )
        )
        {
            System.out.println(
                "File not found."
            );

            return;
        }

        if (
            !response.equals(
                "FILE_START"
            )
        )
        {
            System.out.println(
                "Unexpected server response."
            );

            return;
        }

        String fileName =
            is.readUTF();

        long fileSize =
            is.readLong();

        File folder =
            new File(
                "client_downloads"
            );

        if (!folder.exists())
        {
            folder.mkdir();
        }

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
                    "File download interrupted."
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

        System.out.println();
        System.out.println(
            "File downloaded successfully."
        );

        System.out.println(
            "File name: " +
            fileName
        );

        System.out.println(
            "File size: " +
            fileSize +
            " bytes"
        );

        System.out.println(
            "Saved in: client_downloads"
        );

        System.out.println(
            "Returning to menu..."
        );
    }
}
            