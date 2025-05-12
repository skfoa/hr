package org.example.hr.service;

import org.example.hr.mapper.JobInfMapper;
import org.example.hr.pojo.JobInf;
import org.example.hr.service.impl.JobInfServicelmpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class JobInfService implements JobInfServicelmpl {

    private static final Logger logger = LoggerFactory.getLogger(JobInfService.class);

    private final JobInfMapper jobInfMapper;

    @Autowired
    public JobInfService(JobInfMapper jobInfMapper) {
        this.jobInfMapper = jobInfMapper;
    }

    @Override
    public List<JobInf> getAllJobs() {
        logger.debug("Fetching all jobs");
        return jobInfMapper.findAll();
    }

    @Override
    public JobInf getJobById(Integer jobId) {
        logger.debug("Fetching job by id: {}", jobId);
        if (jobId == null) {
            return null;
        }
        return jobInfMapper.findById(jobId);
    }

    @Override
    public JobInf getJobByName(String jobName) {
        logger.debug("Fetching job by name: {}", jobName);
        if (jobName == null || jobName.trim().isEmpty()) {
            return null;
        }
        return jobInfMapper.findByName(jobName);
    }

    @Override
    @Transactional
    public boolean addJob(JobInf jobInf) {
        if (jobInf == null || jobInf.getJobName() == null || jobInf.getJobName().trim().isEmpty()) {
            logger.warn("Attempted to add job with null or empty name.");
            return false;
        }
        JobInf existingJob = jobInfMapper.findByName(jobInf.getJobName().trim());
        if (existingJob != null) {
            logger.warn("Attempted to add job with existing name: {}", jobInf.getJobName());
            return false;
        }
        try {
            int result = jobInfMapper.insert(jobInf);
            logger.info("Job added successfully: {}, ID: {}", jobInf.getJobName(), jobInf.getJobId());
            return result > 0;
        } catch (Exception e) {
            logger.error("Error adding job: {}", jobInf.getJobName(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateJob(JobInf jobInf) {
        if (jobInf == null || jobInf.getJobId() == null || jobInf.getJobName() == null || jobInf.getJobName().trim().isEmpty()) {
            logger.warn("Attempted to update job with invalid data.");
            return false;
        }
        JobInf currentJob = jobInfMapper.findById(jobInf.getJobId());
        if (currentJob == null) {
            logger.warn("Attempted to update non-existent job with ID: {}", jobInf.getJobId());
            return false;
        }

        JobInf existingJobWithNewName = jobInfMapper.findByName(jobInf.getJobName().trim());
        if (existingJobWithNewName != null && !Objects.equals(existingJobWithNewName.getJobId(), jobInf.getJobId())) {
            logger.warn("Attempted to update job ID {} with a name '{}' that already exists for job ID {}",
                    jobInf.getJobId(), jobInf.getJobName(), existingJobWithNewName.getJobId());
            return false;
        }

        try {
            int result = jobInfMapper.update(jobInf);
            logger.info("Job updated successfully: ID {}", jobInf.getJobId());
            return result > 0;
        } catch (Exception e) {
            logger.error("Error updating job with ID: {}", jobInf.getJobId(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteJobById(Integer jobId) {
        if (jobId == null) {
            logger.warn("Attempted to delete job with null ID.");
            return false;
        }
        JobInf jobToDelete = jobInfMapper.findById(jobId);
        if (jobToDelete == null) {
            logger.warn("Attempted to delete non-existent job with ID: {}", jobId);
            return false;
        }
        try {
            int result = jobInfMapper.deleteById(jobId);
            logger.info("Job deleted successfully: ID {}", jobId);
            return result > 0;
        } catch (Exception e) {
            logger.error("Error deleting job with ID: {}. Possible foreign key constraint.", jobId, e);
            return false;
        }
    }
}