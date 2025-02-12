package com.fooddonation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fooddonation.model.BookingRequest;
import com.fooddonation.model.Donor;
import com.fooddonation.repository.DonorRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Controller
public class DonorController {

    @Autowired
    private DonorRepository donorRepository;

    // Show the donor form
    @GetMapping("/donor")
    public String showDonorForm(Model model) {
        Donor donor = new Donor();
        model.addAttribute("donor", donor);
        return "donor";  // Thymeleaf will render donor.html
    }

    // Handle the form submission
    @PostMapping("/donor")
    public String submitDonation(@ModelAttribute Donor donor, Model model) {
        // Save donor information in the database
        donorRepository.save(donor);

        // Add donor details to the model to pass to the Thymeleaf template
        model.addAttribute("donorName", donor.getDonorName());
        model.addAttribute("quantity", donor.getQuantity());
        model.addAttribute("foodCategory", donor.getFoodCategory());

        // Redirect to the thank-you page
        return "greetings";
    }
    // Show success page
     @GetMapping("/receiver")
    public String viewDonations(@RequestParam(value = "city", required = false) String city, Model model) {
        List<Donor> donors;

        if (city != null && !city.isEmpty()) {
            donors = donorRepository.findByCityIgnoreCase(city);
        } else {
            donors = donorRepository.findAll();
        }

        model.addAttribute("donors", donors);
        return "receiver";
    }
   @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/book")
    public String bookFood(BookingRequest bookingRequest) {
        Donor donor = donorRepository.findById(bookingRequest.getDonorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid donor ID"));

        // Send email
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(donor.getEmail());
            helper.setSubject("Food Booking Confirmation");
            helper.setText(String.format(
                "Dear %s,\n\n We are pleased to inform you that %s has booked your food.Please find the details below\nPickup Time: %s\n\nDetails:\nFood category: %s\nFood Description: %s\nQuantity: %d\nKindly ensure that the food is packed appropriately and handed over to the respective donor in a hygienic manner.\nThank you for your contribution to this noble cause!\n\n Best Regards,\n Smart Food Donation Team",
                donor.getDonorName(),
                bookingRequest.getName(),
                bookingRequest.getPickupTime(),
                donor.getFoodCategory(),
                donor.getFoodDesc(),
                donor.getQuantity()
            ));
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        String donorAddress = donor.getAddress();
        // Remove donor from the database
        donorRepository.delete(donor);

        return "redirect:/receiver?success=true&address=" + donorAddress;
    }
}

    

