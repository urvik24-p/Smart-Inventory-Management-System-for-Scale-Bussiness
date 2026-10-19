package com.inventory.smartinventory.config;

import com.inventory.smartinventory.entity.*;
import com.inventory.smartinventory.repository.ProductRepository;
import com.inventory.smartinventory.repository.SerialNumberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final SerialNumberRepository serialNumberRepository;

    public DataInitializer(ProductRepository productRepository,
                           SerialNumberRepository serialNumberRepository) {
        this.productRepository = productRepository;
        this.serialNumberRepository = serialNumberRepository;
    }

    @Override
    public void run(String... args) {
        seedProductsAndSerials();
    }

    private void seedProductsAndSerials() {
        if (productRepository.count() > 0) {
            return;
        }

        System.out.println(">>> [INIT] Seeding sample products and serial numbers...");

        List<ProductData> list = List.of(
            new ProductData("pH Meter Model HI98130", "Testing Instruments", "Hanna Instruments",
                List.of("PHM-HI98130-001", "PHM-HI98130-002", "PHM-HI98130-003")),
            new ProductData("pH Meter Portable HI98103", "Testing Instruments", "Hanna Instruments",
                List.of("PHM-HI98103-001", "PHM-HI98103-002")),
            new ProductData("Benchtop pH Meter PH-500", "Laboratory Equipment", "Jenway",
                List.of("PHM-PH500-001", "PHM-PH500-002")),
            new ProductData("Digital Thermometer TM-902C", "Measurement Instruments", "Lutron",
                List.of("THM-TM902-001", "THM-TM902-002", "THM-TM902-003", "THM-TM902-004")),
            new ProductData("Infrared Thermometer GM300", "Measurement Instruments", "Benetech",
                List.of("THM-GM300-001", "THM-GM300-002")),
            new ProductData("Precision RTD Thermometer", "Laboratory Equipment", "Fluke",
                List.of("THM-RTD-001", "THM-RTD-002")),
            new ProductData("Temperature Controller TC-100", "Industrial Equipment", "Autonics",
                List.of("TC-AUTNCS-001", "TC-AUTNCS-002", "TC-AUTNCS-003")),
            new ProductData("PID Temperature Controller", "Industrial Equipment", "Omega Engineering",
                List.of("TC-OMEGA-001", "TC-OMEGA-002")),
            new ProductData("Thermohygrometer HT-1000", "Environmental Monitoring", "Testo",
                List.of("THY-HT1000-001", "THY-HT1000-002", "THY-HT1000-003")),
            new ProductData("Digital Thermo-Hygrometer 608", "Environmental Monitoring", "Testo",
                List.of("THY-608-001", "THY-608-002")),
            new ProductData("Digital Barometer DM-9601A", "Environmental Monitoring", "Lutron",
                List.of("BAR-DM9601-001", "BAR-DM9601-002")),
            new ProductData("Precision Barometer PTB110", "Measurement Instruments", "Vaisala",
                List.of("BAR-PTB110-001")),
            new ProductData("Universal Testing Machine UTM-50", "Industrial Equipment", "Tinius Olsen",
                List.of("UTM-50-001")),
            new ProductData("Tensile Testing Machine TT-2000", "Industrial Equipment", "Zwick Roell",
                List.of("TT-2000-001")),
            new ProductData("Analytical Balance AB-204", "Weighing Scales", "Mettler Toledo",
                List.of("SCL-AB204-001", "SCL-AB204-002")),
            new ProductData("Precision Scale PS-3200", "Weighing Scales", "Kern",
                List.of("SCL-PS3200-001", "SCL-PS3200-002")),
            new ProductData("Industrial Platform Scale IPS", "Weighing Scales", "A&D Weighing",
                List.of("SCL-IPS-001"))
        );

        int count = 0;
        for (ProductData pd : list) {
            Product product = new Product();
            product.setProductName(pd.name);
            product.setCategory(pd.category);
            product.setBrand(pd.brand);
            product = productRepository.save(product);

            for (int i = 0; i < pd.serials.size(); i++) {
                String sn = pd.serials.get(i);
                SerialNumber serial = new SerialNumber();
                serial.setProduct(product);
                serial.setSerialNumber(sn);
                // Distribute statuses: last serials marked as sold or damaged for demo
                if (i == pd.serials.size() - 1 && pd.serials.size() > 2) {
                    serial.setStatus(SerialStatus.sold);
                } else if (count % 7 == 0) {
                    serial.setStatus(SerialStatus.damaged);
                } else {
                    serial.setStatus(SerialStatus.available);
                }
                serialNumberRepository.save(serial);
                count++;
            }
        }

        System.out.println(">>> [INIT] Successfully seeded " + list.size() + " products and " + count + " serial numbers.");
    }

    private static class ProductData {
        String name;
        String category;
        String brand;
        List<String> serials;

        ProductData(String name, String category, String brand, List<String> serials) {
            this.name = name;
            this.category = category;
            this.brand = brand;
            this.serials = serials;
        }
    }
}
