package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
import com.example.vouchermanager.Model.Entity.Voucherapplicableproduct;
<<<<<<< HEAD
=======
import com.example.vouchermanager.Model.Entity.VoucherapplicableproductId;
>>>>>>> main
import com.example.vouchermanager.Repository.VoucherApplicableProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.Optional;
>>>>>>> main
import java.util.stream.Collectors;

@Service
public class VoucherApplicableProductServiceImp implements VoucherApplicableProductService {
    @Autowired
    private VoucherApplicableProductRepository repository;

    @Override
    public List<VoucherApplicableProductDTO> findAll() {
        return repository.findAll().stream()
                .map(voucher -> new VoucherApplicableProductDTO(
                        voucher.getVoucherCode().getVoucherCode(),
                        voucher.getProductID().getId()
                ))
                .collect(Collectors.toList());
    }
<<<<<<< HEAD
=======
    public Optional<Voucherapplicableproduct> getById(VoucherapplicableproductId id) {
        return repository.findById(id);
    }

    public Voucherapplicableproduct create(Voucherapplicableproduct voucherApplicableProduct) {
        return repository.save(voucherApplicableProduct);
    }

    public Voucherapplicableproduct update(VoucherapplicableproductId id, Voucherapplicableproduct voucherApplicableProduct) {
        if (repository.existsById(id)) {
            return repository.save(voucherApplicableProduct);
        }
        throw new RuntimeException("VoucherApplicableProduct not found");
    }

    public void delete(VoucherapplicableproductId id) {
        repository.deleteById(id);
    }
>>>>>>> main
}
