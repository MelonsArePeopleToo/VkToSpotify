import vkClient.IVkClient;
import vkClient.VkClient;
import vkRepository.IVkRepository;
import vkRepository.LocalRepository;
import vkRepository.VkRepository;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        IVkClient client = new VkClient();

        Scanner in = new Scanner(System.in);
        if(client.login(in.nextLine(), in.nextLine())) {
            System.out.println("ok");

            IVkRepository repository = new VkRepository(client);
            repository.findAllAudios();

//            repository.getLinks();

            LocalRepository.dropDataIntoFile(repository,
                    "/home/sanchelozzz/Documents/file.json");

        }
    }
}
