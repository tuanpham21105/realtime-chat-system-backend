package com.owl.social_service.application.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.owl.social_service.application.admin.ControlFriendshipAdminServices;


@Service
@Transactional
public class ControlFriendshipUserServices {
    private final GetFriendshipUserServies getFriendshipUserServies;
    private final ControlFriendshipAdminServices controlFriendshipAdminServices;

    public ControlFriendshipUserServices(GetFriendshipUserServies getFriendshipUserServies, ControlFriendshipAdminServices controlFriendshipAdminServices) {
        this.getFriendshipUserServies = getFriendshipUserServies;
        this.controlFriendshipAdminServices = controlFriendshipAdminServices;}
    
    public void deleteFriendship(String requesterId, String id) {
        getFriendshipUserServies.getFrienshipById(requesterId, id);

        controlFriendshipAdminServices.deleteFriendship(id);
    }
}
