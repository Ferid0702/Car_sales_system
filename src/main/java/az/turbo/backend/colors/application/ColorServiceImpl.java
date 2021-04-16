package az.turbo.backend.colors.application;

import az.turbo.backend.colors.application.dto.ColorCreateDto;
import az.turbo.backend.colors.application.dto.ColorDto;
import az.turbo.backend.colors.application.dto.ColorUpdateDto;
import az.turbo.backend.colors.domain.ColorRepository;
import az.turbo.backend.colors.domain.model.Color;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {

    private ColorRepository colorRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ColorServiceImpl(ColorRepository colorRepository, ModelMapper modelMapper) {
        this.colorRepository = colorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ColorDto> retrieveAll() {
        return colorRepository
                .findAll()
                .stream()
                .map(color -> modelMapper.map(color, ColorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public long create(ColorCreateDto colorCreateDto) {
        Color color = modelMapper.map(colorCreateDto, Color.class);
        return colorRepository.create(color);
    }

    @Override
    public void update(ColorUpdateDto colorUpdateDto) {
        Color color = modelMapper.map(colorUpdateDto, Color.class);
        colorRepository.update(color);
    }
}
