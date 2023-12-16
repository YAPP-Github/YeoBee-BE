variable "aws_region" {
  default = "ap-northeast-2"
}

variable "az_count" {
  default = "2"
}

variable "app_port" {
  default = 8080
}

variable "domain" {
  type = string
}

variable "ec2_key_name" {
  sensitive = true
}

variable "health_check_path" {
  default = "/actuator/health"
}

variable "project_name" {
  type = string
}

variable "project_repository" {
  type = string
}

variable "db_username" {
  sensitive = true
}

variable "db_password" {
  sensitive = true
}

variable "db_port" {
  default   = "33306"
  sensitive = true
}

variable "db_instance_class" {
  default = "db.t2.micro"
}

variable "db_allocated_storage" {
  default = 8
}

variable "db_storage_type" {
  default = "gp2"
}

variable "db_engine" {
  default = "mysql"
}

variable "db_engine_version" {
  default = "8.0.33"
}

# variable "db_init_script" {
#   description = "SQL to initialise the RDS instance"
#   default     = "initial-schema.sql"
# }
