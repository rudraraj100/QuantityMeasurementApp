package com.app.quantitymeasurementapp.service;

import com.app.quantitymeasurementapp.dto.QuantityDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.app.quantitymeasurementapp.model.OperationType;
import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.quantity.Quantity;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurementapp.unit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

	private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

	@Autowired
	private QuantityMeasurementRepository repository;

	@SuppressWarnings("unchecked")
	private Quantity<? extends IMeasurable> convertDtoToModel(QuantityDTO dto) {
		String type = dto.getNormalisedMeasurementType();
		String unit = dto.getUnit().toUpperCase();

		return switch (type) {
		case "LENGTH" -> new Quantity<>(dto.getValue(), LengthUnit.valueOf(unit));
		case "WEIGHT" -> new Quantity<>(dto.getValue(), WeightUnit.valueOf(unit));
		case "VOLUME" -> new Quantity<>(dto.getValue(), VolumeUnit.valueOf(unit));
		case "TEMPERATURE" -> new Quantity<>(dto.getValue(), TemperatureUnit.valueOf(unit));
		default -> throw new QuantityMeasurementException("Invalid measurement type: " + dto.getMeasurementType());
		};
	}

	private IMeasurable resolveTargetUnit(String normalisedType, String targetUnitName) {
		String unit = targetUnitName.toUpperCase();
		return switch (normalisedType) {
		case "LENGTH" -> LengthUnit.valueOf(unit);
		case "WEIGHT" -> WeightUnit.valueOf(unit);
		case "VOLUME" -> VolumeUnit.valueOf(unit);
		case "TEMPERATURE" -> TemperatureUnit.valueOf(unit);
		default -> throw new QuantityMeasurementException("Invalid conversion type: " + normalisedType);
		};
	}

	private QuantityMeasurementDTO buildAndSave(Long userId, String userEmail, QuantityDTO q1, QuantityDTO q2,
			String operation, Double resultValue, String resultUnit, String resultMeasurementType, String resultString) {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(userId, userEmail, q1.getValue(), q1.getUnit(),
				q1.getMeasurementType(), q2 != null ? q2.getValue() : null, q2 != null ? q2.getUnit() : null,
				q2 != null ? q2.getMeasurementType() : null, operation, resultValue, resultUnit, resultMeasurementType,
				resultString, false, null);

		QuantityMeasurementEntity saved = repository.save(entity);
		return QuantityMeasurementDTO.fromEntity(saved);
	}

	private QuantityMeasurementDTO buildAndSaveError(Long userId, String userEmail, QuantityDTO q1, QuantityDTO q2,
			String operation, String errorMessage) {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(userId, userEmail, q1.getValue(), q1.getUnit(),
				q1.getMeasurementType(), q2 != null ? q2.getValue() : null, q2 != null ? q2.getUnit() : null,
				q2 != null ? q2.getMeasurementType() : null, operation, null, null, null, null, true, errorMessage);

		QuantityMeasurementEntity saved = repository.save(entity);
		return QuantityMeasurementDTO.fromEntity(saved);
	}

	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail) {
		logger.debug("ADD  {} + {}", q1, q2);
		try {
			Quantity<IMeasurable> qty1 = (Quantity<IMeasurable>) convertDtoToModel(q1);
			Quantity<IMeasurable> qty2 = (Quantity<IMeasurable>) convertDtoToModel(q2);
			Quantity<?> result = qty1.add(qty2);

			return buildAndSave(userId, userEmail, q1, q2, OperationType.ADD.getDisplayName(), result.getValue(),
					result.getUnit().toString(), q1.getMeasurementType(), result.toString());

		} catch (Exception e) {
			logger.warn("ADD error: {}", e.getMessage());
			QuantityMeasurementDTO errDto = buildAndSaveError(userId, userEmail, q1, q2,
					OperationType.ADD.getDisplayName(), "add Error: " + e.getMessage());
			throw new QuantityMeasurementException(errDto.getErrorMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail) {
		logger.debug("SUBTRACT  {} - {}", q1, q2);
		try {
			Quantity<IMeasurable> qty1 = (Quantity<IMeasurable>) convertDtoToModel(q1);
			Quantity<IMeasurable> qty2 = (Quantity<IMeasurable>) convertDtoToModel(q2);
			Quantity<?> result = qty1.subtract(qty2);

			return buildAndSave(userId, userEmail, q1, q2, OperationType.SUBTRACT.getDisplayName(), result.getValue(),
					result.getUnit().toString(), q1.getMeasurementType(), result.toString());

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			logger.warn("SUBTRACT error: {}", e.getMessage());
			QuantityMeasurementDTO errDto = buildAndSaveError(userId, userEmail, q1, q2,
					OperationType.SUBTRACT.getDisplayName(), "subtract Error: " + e.getMessage());
			throw new QuantityMeasurementException(errDto.getErrorMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail) {
		logger.debug("DIVIDE  {} / {}", q1, q2);
		try {
			Quantity<IMeasurable> qty1 = (Quantity<IMeasurable>) convertDtoToModel(q1);
			Quantity<IMeasurable> qty2 = (Quantity<IMeasurable>) convertDtoToModel(q2);
			double result = qty1.divide(qty2);

			return buildAndSave(userId, userEmail, q1, q2, OperationType.DIVIDE.getDisplayName(), result, "RATIO", null,
					String.valueOf(result));

		} catch (ArithmeticException e) {
			logger.warn("DIVIDE arithmetic error: {}", e.getMessage());
			buildAndSaveError(userId, userEmail, q1, q2, OperationType.DIVIDE.getDisplayName(), e.getMessage());
			throw e;
		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			logger.warn("DIVIDE error: {}", e.getMessage());
			QuantityMeasurementDTO errDto = buildAndSaveError(userId, userEmail, q1, q2,
					OperationType.DIVIDE.getDisplayName(), "divide Error: " + e.getMessage());
			throw new QuantityMeasurementException(errDto.getErrorMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO convert(QuantityDTO q, String targetUnit, Long userId, String userEmail) {
		logger.debug("CONVERT  {} -> {}", q, targetUnit);
		try {
			Quantity<IMeasurable> qty = (Quantity<IMeasurable>) convertDtoToModel(q);
			IMeasurable target = resolveTargetUnit(q.getNormalisedMeasurementType(), targetUnit);
			Quantity<?> converted = qty.convertTo((IMeasurable) target);
			QuantityDTO targetDto = new QuantityDTO(0.0, targetUnit, q.getMeasurementType());

			return buildAndSave(userId, userEmail, q, targetDto, OperationType.CONVERT.getDisplayName(),
					converted.getValue(), converted.getUnit().toString(), q.getMeasurementType(), converted.toString());

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			logger.warn("CONVERT error: {}", e.getMessage());
			QuantityMeasurementDTO errDto = buildAndSaveError(userId, userEmail, q, null,
					OperationType.CONVERT.getDisplayName(), "convert Error: " + e.getMessage());
			throw new QuantityMeasurementException(errDto.getErrorMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2, Long userId, String userEmail) {
		logger.debug("COMPARE  {} == {}", q1, q2);
		try {
			Quantity<? extends IMeasurable> qty1 = convertDtoToModel(q1);
			Quantity<? extends IMeasurable> qty2 = convertDtoToModel(q2);

			double base1 = qty1.getUnit().convertToBaseUnit(qty1.getValue());
			double base2 = qty2.getUnit().convertToBaseUnit(qty2.getValue());
			boolean result = Math.abs(base1 - base2) < 0.0001;

			return buildAndSave(userId, userEmail, q1, q2, OperationType.COMPARE.getDisplayName(), null, null, null,
					String.valueOf(result));

		} catch (QuantityMeasurementException e) {
			throw e;
		} catch (Exception e) {
			logger.warn("COMPARE error: {}", e.getMessage());
			QuantityMeasurementDTO errDto = buildAndSaveError(userId, userEmail, q1, q2,
					OperationType.COMPARE.getDisplayName(), "compare Error: " + e.getMessage());
			throw new QuantityMeasurementException(errDto.getErrorMessage());
		}
	}

	@Override
	public List<QuantityMeasurementDTO> getHistoryByOperation(Long userId, String operation) {
		logger.debug("Getting history for user={} operation={}", userId, operation);
		return QuantityMeasurementDTO.fromEntityList(
				repository.findByUserIdAndOperationOrderByCreatedAtDesc(userId, operation.toLowerCase()));
	}

	@Override
	public List<QuantityMeasurementDTO> getHistoryByMeasurementType(Long userId, String measurementType) {
		logger.debug("Getting history for user={} type={}", userId, measurementType);
		return QuantityMeasurementDTO.fromEntityList(
				repository.findByUserIdAndThisMeasurementTypeOrderByCreatedAtDesc(userId, measurementType));
	}

	@Override
	public long getOperationCount(Long userId, String operation) {
		logger.debug("Counting operation for user={} operation={}", userId, operation);
		return repository.countByUserIdAndOperationAndErrorFalse(userId, operation.toLowerCase());
	}

	@Override
	public List<QuantityMeasurementDTO> getErrorHistory(Long userId) {
		logger.debug("Getting error history for user={}", userId);
		return QuantityMeasurementDTO.fromEntityList(repository.findByUserIdAndErrorTrueOrderByCreatedAtDesc(userId));
	}
}
