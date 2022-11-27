package com.game.controller;

import com.game.dto.PlayerDTO;
import com.game.model.Player;
import com.game.service.PlayerService;
import com.game.utils.PlayerDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerDTOValidator playerDTOValidator;


    @Autowired
    public PlayerController(PlayerService playerService, PlayerDTOValidator playerDTOValidator) {
        this.playerService = playerService;
        this.playerDTOValidator = playerDTOValidator;
    }


    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> allRequestParams) {

        List<Player> playersList = playerService.getSortedAndOrderedPlayersList(allRequestParams);

        return new ResponseEntity<>(playersList, HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam Map<String, String> allRequestParams) {

        List<Player> playersList = playerService.getPlayersList(allRequestParams);

        return new ResponseEntity<>(playersList.size(), HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable("id") Long id) {
        if(id<=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Player> opt = playerService.getPlayerById(id);
        return opt.map(player -> new ResponseEntity<>(player, HttpStatus.OK)).orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/players")
    public ResponseEntity<Player> addPlayer(@RequestBody PlayerDTO playerDTO, BindingResult bindingResult) {
        try {
            playerDTOValidator.validate(playerDTO, bindingResult);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player player = convertToPlayer(playerDTO);
        playerService.addPlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<HttpStatus> removePlayer(@PathVariable("id") @Validated String sid) {
        try{
            double id = Double.parseDouble(sid);
            if(!(id%1==0)||id<=0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!playerService.getPlayerById(Long.parseLong(sid)).isPresent()){
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        playerService.removePlayer(Long.parseLong(sid));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") Long id,
                                               @RequestBody PlayerDTO playerDTO,
                                               BindingResult bindingResult) {

        if(id<=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(!playerService.getPlayerById(id).isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Player playerToBeUpdated = playerService.getPlayerById(id).get();
        PlayerDTO playerDTOToBeUpdated = convertToPlayerDTO(playerToBeUpdated);

        updatePlayerDTO(playerDTOToBeUpdated, playerDTO);
        // Field[] fields = playerDTO.getClass().getFields();
        playerDTOValidator.validate(playerDTOToBeUpdated, bindingResult);
        if(bindingResult.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Player resultPlayer = convertToPlayer(playerDTOToBeUpdated);
        resultPlayer.setId(id);

        System.out.println(playerToBeUpdated);
        System.out.println(resultPlayer);

        if(playerToBeUpdated.equals(resultPlayer)) {
            return new ResponseEntity<>(playerToBeUpdated, HttpStatus.OK);
        }

        playerToBeUpdated = convertToPlayer(playerDTOToBeUpdated);

        playerService.updatePlayer(id, playerToBeUpdated);

        return new ResponseEntity<>(playerToBeUpdated, HttpStatus.OK);
    }

    private Player convertToPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setTitle(playerDTO.getTitle());
        player.setRace(playerDTO.getRace());
        player.setProfession(playerDTO.getProfession());
        player.setBirthday(playerDTO.getBirthday());
        player.setBanned(playerDTO.getBanned());
        player.setExperience(playerDTO.getExperience());

        player.calculateLevel();
        player.calculateUntilNextLevel();

        return player;
    }

    private PlayerDTO convertToPlayerDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();

        playerDTO.setBanned(player.getBanned());
        playerDTO.setBirthday(player.getBirthday());
        playerDTO.setExperience(player.getExperience());
        playerDTO.setName(player.getName());
        playerDTO.setProfession(player.getProfession());
        playerDTO.setRace(player.getRace());
        playerDTO.setTitle(player.getTitle());

        return playerDTO;
    }

    private void updatePlayerDTO(PlayerDTO fromBase, PlayerDTO updateData) {
        if(updateData.getBanned()!= null) fromBase.setBanned(updateData.getBanned());
        if(updateData.getBirthday()!= null) fromBase.setBirthday(updateData.getBirthday());
        if(updateData.getExperience()!= null) fromBase.setExperience(updateData.getExperience());
        if(updateData.getName()!= null) fromBase.setName(updateData.getName());
        if(updateData.getProfession()!= null) fromBase.setProfession(updateData.getProfession());
        if(updateData.getRace()!= null) fromBase.setRace(updateData.getRace());
        if(updateData.getTitle()!= null) fromBase.setTitle(updateData.getTitle());
    }
}
