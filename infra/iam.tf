resource "aws_iam_user" "api_s3_user" {
  name = "${var.project_name}-api-s3-user"
}

resource "aws_iam_access_key" "user_key" {
  user = aws_iam_user.api_s3_user.name
}

resource "aws_iam_user_policy_attachment" "s3_policy" {
  user       = aws_iam_user.api_s3_user.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
}

output "access_key" {
  value     = aws_iam_access_key.user_key.id
  sensitive = true
}

output "secret_key" {
  value     = aws_iam_access_key.user_key.secret
  sensitive = true
}
