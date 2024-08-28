package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(ProfileRestController.class)
@SpringBootTest
//@AutoConfigureMockMvc
class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private ProfileRestController profileRestController;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getProfile_when_IsSuccess_User() throws Exception {

        // Успішний сценарій для GET запиту
        ProfileTo profileTo = new ProfileTo(1L, Set.of("NOTIFICATION_1", "NOTIFICATION_2"),
                Set.of(new ContactTo("email", "test@example.com")));

        when(profileRestController.get(any(AuthUser.class))).thenReturn(profileTo);

        mockMvc.perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()) // Додано для друку відповіді
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.mailNotifications[0]").value("NOTIFICATION_1"))
                .andExpect(jsonPath("$.contacts[0].code").value("email"))
                .andExpect(jsonPath("$.contacts[0].value").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getProfile_when_User_NotFound() throws Exception {
        // Неуспішний сценарій для GET запиту (наприклад, профіль не знайдено)
        when(profileRestController.get(any(AuthUser.class)))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get(ProfileRestController.REST_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void update_Profile_when_User_Success() throws Exception {
        // Успішний сценарій для PUT запиту
        ProfileTo profileTo = new ProfileTo(1L, Set.of("NOTIFICATION_1"),
                Set.of(new ContactTo("email", "test@example.com")));

        doNothing().when(profileRestController).update(any(ProfileTo.class), any(AuthUser.class));

        mockMvc.perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "mailNotifications": ["NOTIFICATION_1"],
                          "contacts": [{"code": "email", "value": "test@example.com"}]
                        }
                        """))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void update_Profile_when_BadRequest() throws Exception {
        // Неуспішний сценарій для PUT запиту (наприклад, некоректні дані)
        mockMvc.perform(MockMvcRequestBuilders.put(ProfileRestController.REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "mailNotifications": [""],
                          "contacts": [{"code": "email", "value": ""}]
                        }
                        """))  // Некоректні дані
                .andExpect(status().isUnprocessableEntity());  // Очікує 422
                //.andExpect(status().isBadRequest()); // Очікувалося 400
    }
}