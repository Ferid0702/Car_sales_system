package az.turbo.backend.colors;

import az.turbo.backend.colors.application.ColorService;
import az.turbo.backend.colors.application.dto.ColorCreateDto;
import az.turbo.backend.colors.application.dto.ColorDto;

import az.turbo.backend.colors.application.dto.ColorUpdateDto;
import az.turbo.backend.shared.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/colors")
public class ColorController {
    private ColorService colorService;

    @Autowired
    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping(value = "/retrieve-all")
    @ResponseBody
    public List<ColorDto> retrieveAll() {
        return colorService.retrieveAll();
    }

    @GetMapping(value = "/retrieve-by-id/{id}")
    @ResponseBody
    public ColorUpdateDto retrieveById(@PathVariable Long id) {
        return colorService.retrieveById(id);
    }

    @PostMapping(value = "/create")
    public long create(@Valid @RequestBody ColorCreateDto colorCreateDto) {
        colorCreateDto.setCreatedBy(UserContextHolder.getUserId());
        colorCreateDto.setCreatedDate(LocalDateTime.now());
        return colorService.create(colorCreateDto);
    }

    @PutMapping(value = "/update")
    public void update(@Valid @RequestBody ColorUpdateDto colorUpdateDto) {
        colorService.update(colorUpdateDto);
    }

    @DeleteMapping(value = "/deleted/{id}")
    public void delete(@PathVariable Long id) {
        colorService.deleteById(id, 1, LocalDateTime.now());
    }
}
