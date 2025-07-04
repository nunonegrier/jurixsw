package br.com.jurix.dashboard.controller;

import br.com.jurix.dashboard.dto.DashBoardDTO;
import br.com.jurix.dashboard.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/dashboard")
public class DashBoardController {

    @Autowired
    DashBoardService service;


    @GetMapping
    public DashBoardDTO buscarDashBoard(){
        return service.buscarDashBoard();
    }
}
