package com.restaubot.spring.controllers;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.restaubot.spring.models.dto.NoteDTO;
import com.restaubot.spring.security.CustomRuntimeException;
import com.restaubot.spring.services.NoteService;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    private static final Logger logger = LogManager.getLogger(NoteController.class);
    private static final String UNEXPECTED_EXCEPTION = "Unexpected Exception : {}";

    @Autowired
    NoteService noteService;

    @PostMapping("/createNote/{restaurantId}")
    public ResponseEntity<List<NoteDTO>> createNote(@RequestBody NoteDTO noteDto, @PathVariable Integer restaurantId) throws CustomRuntimeException {
        logger.info("Process request: Create Note");

        // Récupérer les identifiants du restaurant et du client depuis noteDto
        Integer idCustomer = noteDto.getCustomer().getIdUser();

        // Vérifier si une note existe déjà pour le restaurant et le client spécifiés
        List<NoteDTO> existingNote = noteService.findNoteByRestaurantAndCustomer(restaurantId,
                idCustomer);
        if (!existingNote.isEmpty()) {
            logger.warn("Note already exists for restaurant {} and customer {}", restaurantId, idCustomer);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            // Créer une nouvelle note s'il n'y a pas de note existante
            try {
                noteService.createNote(noteDto);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (CustomRuntimeException e) {
                if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
                    logger.warn(e.getMessage());
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }
        }
    }

    @GetMapping("/averageNote/{restaurantId}")
public ResponseEntity<Double> getAverageNote(@PathVariable Integer restaurantId) {
    logger.info("Process request: Get Average Note");

    try {
        Double averageNote = noteService.findAverageNoteByRestaurant(restaurantId);

        if (averageNote != null) {
            return new ResponseEntity<>(averageNote, HttpStatus.OK);
        } else {
            // Gérer le cas où aucune note n'est disponible pour le restaurant spécifié
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } catch (CustomRuntimeException e) {
        if (e.getMessage().equals(CustomRuntimeException.SERVICE_ERROR)) {
            logger.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.error(UNEXPECTED_EXCEPTION, e.getMessage());
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
}
