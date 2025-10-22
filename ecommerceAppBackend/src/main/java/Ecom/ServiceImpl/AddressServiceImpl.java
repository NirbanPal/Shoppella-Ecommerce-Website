package Ecom.ServiceImpl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ecom.Exception.AddressException;
import Ecom.Exception.UserException;
import Ecom.Model.Address;
import Ecom.Model.User;
import Ecom.Repository.AddressRepository;
import Ecom.Repository.UserRepository;
import Ecom.Service.AddressService;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    public Address addAddressToUser(Integer userId, Address address) throws AddressException {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        Address savedAddress = addressRepository.save(address);
        savedAddress.setUser(existingUser);

        existingUser.getAddress().add(savedAddress);
        userRepository.save(existingUser);
        return savedAddress;
    }

    @Override
    public Address updateAddress(Address address,Integer addressId) throws AddressException {

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found"));


        existingAddress.setFlatNo(address.getFlatNo());
        existingAddress.setZipCode(address.getZipCode());
        existingAddress.setStreet(address.getStreet());
        existingAddress.setCity(address.getCity());
        existingAddress.setState(address.getState());
        return addressRepository.save(existingAddress);
    }

    @Override
    public void removeAddress(Integer addressId) throws AddressException {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressException("Address not found"));

        addressRepository.delete(existingAddress);
    }

    @Override
    public List<Address> getAllUserAddress(Integer userId) throws AddressException {
        List<Address> userAddressList = addressRepository.getUserAddressList(userId);
        if (userAddressList.isEmpty()) {
            System.out.println("empty");
            throw new AddressException("User does not have any address");
        }
        return userAddressList;
    }


}
