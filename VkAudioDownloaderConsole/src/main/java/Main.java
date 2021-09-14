import vkClient.IVkClient;
import vkClient.VkClient;
import vkRepository.VkRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IVkClient client = new VkClient();

        Scanner in = new Scanner(System.in);
        if(client.login(in.nextLine(), in.nextLine())) {
            System.out.println("ok");
        }

        VkRepository repository = new VkRepository(client);
        repository.findAllAudios();
    }
}
