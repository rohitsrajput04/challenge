package com.dws.challenge.web;

import com.dws.challenge.service.NotificationService;
import com.dws.challenge.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @MockBean
    private NotificationService notificationService; // Mocked bean

    @BeforeEach
    void setUp() {
        Mockito.doNothing().when(transferService).transferMoney(Mockito.anyString(), Mockito.anyString(), Mockito.any(BigDecimal.class));
    }

    @Test
    void testTransferMoney() throws Exception {
        this.mockMvc.perform(post("/accounts/transfer")
                        .param("accountFromId", "1")
                        .param("accountToId", "2")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful."));
    }

}
