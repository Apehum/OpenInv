#!/bin/bash
#
# Copyright (C) 2011-2022 lishid. All rights reserved.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, version 3.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#

# Note that this script is designed for use in GitHub Actions, and is not
# particularly robust nor configurable. Run from project parent directory.

# Get a pretty list of supported Minecraft versions
readarray -t versions <<< "$(. ./scripts/get_spigot_versions.sh)"

for version in "${versions[@]}"; do
  # Append comma if variable is set, then append version
  minecraft_versions="${minecraft_versions:+${minecraft_versions}, }${version%%-R*}"
done

echo "${minecraft_versions}"
